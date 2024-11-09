package com.tpKafka_grupo10.service;

import java.time.LocalDate;
import java.util.Collections;
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
	    // Establecer fecha de solicitud
	    ordenCompra.setFechaSolicitud(LocalDate.now());

	    // Validar los ítems
	    if (validarOrdenCompra(ordenCompra)) {
	        // Si la orden es válida, establecer estado y asociar ítems
	        ordenCompra.setEstado(EstadoOrden.SOLICITADA);

	        // Asignar la referencia de OrdenCompra a cada ItemOrdenCompra
	        for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
	            item.setOrdenCompra(ordenCompra);
	        }
	    } else {
	        // Si la orden es rechazada, establecer estado sin asociar ítems
	        ordenCompra.setEstado(EstadoOrden.RECHAZADA);
	        ordenCompra.setObservaciones("Orden de Compra Rechazada");
	        
	        // Limpiar los ítems si la orden es rechazada
	        ordenCompra.setItemsOrdenCompra(Collections.emptyList());
	    }

	    // Guardar en la base de datos
	    OrdenCompra ordenGuardada = ordenCompraRepository.save(ordenCompra);

	    // Enviar el mensaje a Kafka sin importar el estado de la orden
	    try {
	        String mensaje = generarMensajeKafka(ordenGuardada);
	        kafkaTemplateString.send("orden-de-compra", mensaje);
	    } catch (Exception e) {
	        Logger logger = LoggerFactory.getLogger(this.getClass());
	        logger.error("Error al enviar mensaje a Kafka", e);
	        throw new RuntimeException("Error al enviar mensaje a Kafka", e);
	    }

	    return ordenGuardada;
	}


	// Método para calcular la cantidad total (puedes ajustar esto según tu lógica)
	private int calcularCantidadTotal(List<ItemOrdenDeCompra> items) {
		int total = 0;
		for (ItemOrdenDeCompra item : items) {
			total += item.getCantidad(); // Asumiendo que tienes un método getCantidad en ItemOrdenDeCompra
		}
		return total;
	}

	private boolean validarOrdenCompra(OrdenCompra ordenCompra) {
		boolean ordenValida = true;
		if (ordenCompra.getItemsOrdenCompra() == null || ordenCompra.getItemsOrdenCompra().isEmpty()) {
			ordenValida = false;
		}

		for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
			if (item.getCantidad() <= 0) {
				ordenValida = false;
			}
		}
		return ordenValida;
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
