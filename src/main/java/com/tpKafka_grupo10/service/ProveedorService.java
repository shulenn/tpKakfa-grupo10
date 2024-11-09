package com.tpKafka_grupo10.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.interfaces.ProveedorServiceInterface;
import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.ItemDespacho;
import com.tpKafka_grupo10.model.ItemOrdenDeCompra;
import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.model.OrdenDespacho;
import com.tpKafka_grupo10.model.Producto;
import com.tpKafka_grupo10.model.Stock;
import com.tpKafka_grupo10.repository.ItemDespachoRepository;
import com.tpKafka_grupo10.repository.OrdenCompraRepository;
import com.tpKafka_grupo10.repository.OrdenDespachoRepository;
import com.tpKafka_grupo10.repository.StockRepository;

@Transactional
@Service
public class ProveedorService implements ProveedorServiceInterface {

	private final KafkaTemplate<String, String> kafkaTemplateString;
	private final KafkaTemplate<String, StockUpdateEvent> kafkaTemplateEvent;

	@Autowired
	public ProveedorService(KafkaTemplate<String, String> kafkaTemplateString,
			KafkaTemplate<String, StockUpdateEvent> kafkaTemplateEvent) {
		this.kafkaTemplateString = kafkaTemplateString;
		this.kafkaTemplateEvent = kafkaTemplateEvent;
	}

	@Autowired
	private StockService stockService; // Un servicio para manejar stock

	@Autowired
	private OrdenDespachoRepository ordenDespachoRepository;

	@Autowired
	private ItemDespachoRepository itemDespachoRepository;

	@Autowired
	private OrdenCompraRepository ordenCompraRepository;

	@Autowired
	private StockRepository stockRepository;

	public void procesarOrdenCompra(OrdenCompra ordenCompra) {
		String codigoTienda = ordenCompra.getTiendaId().toString();

		// Validar artículos y obtener errores
		List<String> errores = validarItemsOrdenDeCompra(ordenCompra);

		// Si hay errores, rechazar la orden y enviar el mensaje de error
		if (!errores.isEmpty()) {
			rechazarOrdenConErrores(ordenCompra, errores, codigoTienda);
			return; // Salir del método
		}

		// Verificar si hay stock suficiente para cada artículo
		List<ItemOrdenDeCompra> faltantes = verificarStock(ordenCompra);

		// Si faltan artículos, aceptar la orden parcialmente y enviar notificación
		if (!faltantes.isEmpty()) {
			aceptarOrdenConFaltantes(ordenCompra, faltantes, codigoTienda);
			return; // Salir del método
		}

		// Aceptar completamente la orden, generar despacho y restar stock
		aceptarYGenerarDespacho(ordenCompra, codigoTienda);
	}

	// Método para validar los artículos de la orden
	private List<String> validarItemsOrdenDeCompra(OrdenCompra ordenCompra) {
		List<String> errores = new ArrayList<>();

		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			if (!stockService.proveedorProveeProducto(item.getProducto().getCodigo())) {
				errores.add("Artículo " + item.getProducto().getCodigo() + ": no existe");
			}
			if (item.getCantidad() < 1) {
				errores.add("Articulo " + item.getProducto().getCodigo() + ": cantidad mal informada");
			}
		}
		return errores;
	}

	// Método para rechazar una orden y enviar errores a Kafka
	private void rechazarOrdenConErrores(OrdenCompra ordenCompra, List<String> errores, String codigoTienda) {
		ordenCompra.setEstado(EstadoOrden.RECHAZADA);
		ordenCompraRepository.save(ordenCompra); // Asegúrate de guardar los cambios
		String observacion = String.join(", ", errores);
		kafkaTemplateString.send(codigoTienda + "-solicitudes", observacion);
	}

	// Método para verificar si hay stock suficiente
	private List<ItemOrdenDeCompra> verificarStock(OrdenCompra ordenCompra) {
		List<ItemOrdenDeCompra> faltantes = new ArrayList<>();

		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			if (!stockService.tieneStockSuficiente(item.getProducto().getCodigo(), item.getCantidad())) {
				faltantes.add(item);
			}
		}
		return faltantes;
	}

	// Método para aceptar una orden con faltantes y enviar notificación
	@Transactional
	private void aceptarOrdenConFaltantes(OrdenCompra ordenCompra, List<ItemOrdenDeCompra> faltantes, String codigoTienda) {
		// Actualizar solo el estado y pausada, sin tocar otros campos
		ordenCompraRepository.actualizarEstadoYPausada(ordenCompra.getCodigo(), EstadoOrden.ACEPTADA, true);

		// Preparar el mensaje de observación
		String observacion = "Codigo de Articulos faltantes: " + faltantes.stream()
				.map(item -> String.valueOf(item.getProducto().getCodigo())).collect(Collectors.joining(", "));

		// Enviar el mensaje a Kafka
		kafkaTemplateString.send(codigoTienda + "-solicitudes", observacion);
	}

	// Método para aceptar completamente la orden, generar despacho y restar stock
	private void aceptarYGenerarDespacho(OrdenCompra ordenCompra, String codigoTienda) {
		ordenCompra.setEstado(EstadoOrden.ACEPTADA);
		ordenCompraRepository.save(ordenCompra);
		kafkaTemplateString.send(codigoTienda + "-solicitudes", "Orden aceptada");

		// Generar orden de despacho
		OrdenDespacho ordenDespacho = new OrdenDespacho();
		ordenDespacho.setIdOrdenCompra(ordenCompra.getCodigo());
		ordenDespacho.setFechaEstimadaEnvio(LocalDate.now().plusDays(2)); // Estimación de 2 días

		// Guardar orden de despacho en la base de datos
		ordenDespacho = ordenDespachoRepository.save(ordenDespacho);

		// Crear los items de despacho basados en los items de la orden de compra
		for (ItemOrdenDeCompra itemOrden : ordenCompra.getItemsOrdenCompra()) {
			ItemDespacho itemDespacho = new ItemDespacho();

			// Asignar el producto a ItemDespacho
			Producto producto = itemOrden.getProducto(); // Obtener el objeto Producto
			itemDespacho.setProducto(producto); // Asignar el objeto Producto al ItemDespacho

			// Asignar la cantidad
			itemDespacho.setCantidad(itemOrden.getCantidad());

			// Asignar la orden de despacho
			itemDespacho.setOrdenDespacho(ordenDespacho);

			// Guardar el item de despacho en la base de datos
			itemDespachoRepository.save(itemDespacho);
		}

		// Enviar la orden de despacho a Kafka
		kafkaTemplateString.send(codigoTienda + "-despacho", ordenDespacho.toString());

		// Restar stock del proveedor
		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			stockService.restarStock(item.getProducto().getCodigo(), item.getCantidad());
		}
	}

	// Método para verificar si una orden puede ser cumplida
	public boolean puedeCumplirOrden(OrdenCompra orden) {
		// Implementa la lógica según tu necesidad
		List<ItemOrdenDeCompra> items = orden.getItemsOrdenCompra(); // Asegúrate de que tienes acceso a los ítems
		for (ItemOrdenDeCompra item : items) {
			Stock stock = stockRepository.findByProductoCodigo(item.getProducto().getCodigo());
			if (stock == null || stock.getCantidad() < item.getCantidad()) {
				return false; // No se puede cumplir si no hay suficiente stock
			}
		}
		return true; // Se puede cumplir la orden
	}

	@Override
	public boolean proveedorProveeProducto(Long productoId) {
		return stockRepository.findByProductoCodigo(productoId) != null;
	}
	
	@KafkaListener(topics = "stock-actualizado", groupId = "grupo-proveedor")
	public void manejarActualizacionStock(StockUpdateEvent event) {
		List<OrdenCompra> ordenesPausadas = ordenCompraRepository.findOrdenesPausadasPorProducto(event.getProductoId());
		for (OrdenCompra orden : ordenesPausadas) {
			if (puedeCumplirOrden(orden)) {
				orden.setEstado(EstadoOrden.ACEPTADA);
				orden.setPausada(false);
				ordenCompraRepository.save(orden);
			}
		}
	}
}
