package com.tpKafka_grupo10.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.ItemOrdenDeCompra;
import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.model.OrdenDespacho;

@Service
public class ProveedorService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private StockService stockService; // Un servicio para manejar stock

	public void procesarOrdenCompra(OrdenCompra ordenCompra) {
		List<String> errores = new ArrayList<>();
		boolean todoOk = true;

		// Validar artículos
		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			if (!stockService.proveedorProveeProducto(item.getProducto().getCodigo())) {
				errores.add("Artículo " + item.getProducto().getCodigo() + ": no existe");
				todoOk = false;
			}
			if (item.getCantidad() < 1) {
				errores.add("Artículo " + item.getProducto().getCodigo() + ": cantidad mal informada");
				todoOk = false;
			}
		}

		String codigoTienda = ordenCompra.getTiendaId().toString();

		if (!todoOk) {
			// Cambiar estado de la orden a RECHAZADA
			ordenCompra.setEstado(EstadoOrden.RECHAZADA);
			String observacion = String.join(", ", errores);
			// Enviar mensaje al topic de solicitudes
			kafkaTemplate.send(codigoTienda + "-solicitudes", observacion);
			return; // Salir del método
		}

		// Verificar stock
		List<ItemOrdenDeCompra> faltantes = new ArrayList<>();
		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			if (!stockService.tieneStockSuficiente(item.getProducto().getCodigo(), item.getCantidad())) {
				faltantes.add(item);
				todoOk = false;
			}
		}

		if (!todoOk) {
			// Orden aceptada pero con artículos faltantes
			ordenCompra.setEstado(EstadoOrden.ACEPTADA);
			String observacion = "Artículos faltantes: "
					+ faltantes.stream().map(item -> String.valueOf(item.getProducto().getCodigo())).collect(Collectors.joining(", "));
			// Enviar mensaje al topic de solicitudes
			kafkaTemplate.send(codigoTienda + "-solicitudes", observacion);
			return; // Salir del método
		}

		// Si se puede cumplir con todo el pedido
		ordenCompra.setEstado(EstadoOrden.ACEPTADA);
		// Enviar mensaje al topic de solicitudes
		kafkaTemplate.send(codigoTienda + "-solicitudes", "Orden aceptada");

		// Generar orden de despacho
		OrdenDespacho ordenDespacho = new OrdenDespacho();
		ordenDespacho.setId(UUID.randomUUID().getLeastSignificantBits()); // Generar ID único
		ordenDespacho.setIdOrdenCompra(ordenCompra.getCodigo());
		ordenDespacho.setFechaEstimadaEnvio(LocalDate.now().plusDays(2)); // Por ejemplo, 2 días de estimación

		// Enviar orden de despacho al topic
		kafkaTemplate.send(codigoTienda + "-despacho", ordenDespacho.toString());

		// Restar stock del proveedor
		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			stockService.restarStock(item.getProducto().getCodigo(), item.getCantidad());
		}
	}

}
