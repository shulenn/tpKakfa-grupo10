package com.tpKafka_grupo10.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.kafka.producer.ProducerService;
import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.ItemOrdenDeCompra;
import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.repository.OrdenCompraRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class OrdenCompraService {

	@Autowired
	private OrdenCompraRepository ordenCompraRepository;

	@Autowired
	ProducerService producerService;

	private final KafkaTemplate<String, StockUpdateEvent> kafkaTemplateEvent ;
	private final KafkaTemplate<String, String> kafkaTemplateString;
	
	@PersistenceContext
	private EntityManager entityManager;

	public OrdenCompraService(KafkaTemplate<String, String> kafkaTemplateString, KafkaTemplate<String, StockUpdateEvent> kafkaTemplateEvent) {
		this.kafkaTemplateString = kafkaTemplateString;
		this.kafkaTemplateEvent = kafkaTemplateEvent;
	}

	@Transactional
	public OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra) {
		validarOrdenCompra(ordenCompra); // Validar los ítems

		// Establecer fecha de solicitud y estado inicial
		ordenCompra.setFechaSolicitud(LocalDate.now());
		ordenCompra.setEstado(EstadoOrden.SOLICITADA);

		// Asignar la referencia de OrdenCompra a cada ItemOrdenCompra
		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			item.setOrdenCompra(ordenCompra); // Asignar la referencia
		}

		// Guardar en la base de datos
		OrdenCompra ordenGuardada = ordenCompraRepository.save(ordenCompra);

		try {
			String mensaje = generarMensajeKafka(ordenGuardada);
			kafkaTemplateString.send("orden-de-compra", mensaje);

			return ordenGuardada;
		} catch (Exception e) {
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Error al crear la orden de compra", e);
			throw new RuntimeException("Error al crear la orden de compra", e);
		}
	}

	// Método para calcular la cantidad total (puedes ajustar esto según tu lógica)
	private int calcularCantidadTotal(List<ItemOrdenDeCompra> items) {
		int total = 0;
		for (ItemOrdenDeCompra item : items) {
			total += item.getCantidad(); // Asumiendo que tienes un método getCantidad en ItemOrdenDeCompra
		}
		return total;
	}

	private void validarOrdenCompra(OrdenCompra ordenCompra) {
		if (ordenCompra.getItemsOrdenCompra() == null || ordenCompra.getItemsOrdenCompra().isEmpty()) {
			throw new IllegalArgumentException("La orden de compra no puede ser nula y debe tener al menos un ítem.");
		}

		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			if (item.getProducto() == null) {
				throw new IllegalArgumentException("Cada ítem debe tener un producto asociado.");
			}
		}
	}

	public OrdenCompra modificarOrdenCompra(Long id, OrdenCompra nuevaOrdenCompra) {
		// Buscar la orden de compra existente por ID
		Optional<OrdenCompra> ordenExistente = ordenCompraRepository.findById(id);

		if (!ordenExistente.isPresent()) {
			throw new EntityNotFoundException("No se encontró la orden de compra con ID: " + id);
		}

		// Obtener la orden de compra existente
		OrdenCompra ordenCompra = ordenExistente.get();

		// Aplicar los cambios
		ordenCompra.setFechaSolicitud(nuevaOrdenCompra.getFechaSolicitud());
		ordenCompra.setEstado(nuevaOrdenCompra.getEstado());
		ordenCompra.setItemsOrdenCompra(nuevaOrdenCompra.getItemsOrdenCompra());
		// Otros campos que se puedan modificar...

		// Guardar la orden de compra modificada
		return ordenCompraRepository.save(ordenCompra);
	}

	public void eliminarOrdenCompra(Long id) {
		// Verificar si la orden de compra existe antes de eliminar
		Optional<OrdenCompra> ordenCompra = ordenCompraRepository.findById(id);

		if (!ordenCompra.isPresent()) {
			throw new EntityNotFoundException("No se encontró la orden de compra con ID: " + id);
		}

		// Eliminar la orden de compra
		ordenCompraRepository.deleteById(id);
	}
	
	private String generarMensajeKafka(OrdenCompra orden) {
	    // Convertir la lista de items a un formato de texto
	    String itemsString = orden.getItemsOrdenCompra().stream()
	        .map(item -> String.format("{Id: %s, Talle: %s, Cantidad: %s}", 
	                item.getCodigo(), item.getProducto().getTalle(), item.getCantidad()))
	        .collect(Collectors.joining(", "));

	    // Crear un mensaje con los datos requeridos
	    return String.format("CodigoTienda:%s, IdOrden:%s, Items:[%s], FechaSolicitud:%s", 
	            orden.getTiendaId(), orden.getCodigo(), itemsString, orden.getFechaSolicitud().toString());
	}
}
