package com.tpKafka_grupo10.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.ItemOrdenDeCompra;
import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.repository.OrdenCompraRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class OrdenCompraService {
	@Autowired
	private OrdenCompraRepository ordenCompraRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra) {
	    validarOrdenCompra(ordenCompra); // Validar los ítems

	    // Establecer fecha de solicitud y estado inicial
	    ordenCompra.setFechaSolicitud(LocalDate.now());
	    ordenCompra.setEstado(EstadoOrden.SOLICITADA);

	    // Asignar la referencia de OrdenCompra a cada ItemOrdenDeCompra
	    for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
	        item.setOrdenCompra(ordenCompra); // Asignar la referencia
	    }

	    // Guardar en la base de datos
	    OrdenCompra ordenGuardada = ordenCompraRepository.save(ordenCompra);

	    try {
	        // Enviar mensaje al topic
	        String mensaje = generarMensajeKafka(ordenGuardada);
	        kafkaTemplate.send("/orden-de-compra", mensaje);
	        
	        return ordenGuardada;
	    } catch (Exception e) {
	        Logger logger = LoggerFactory.getLogger(this.getClass());
	        logger.error("Error al crear la orden de compra", e);
	        throw new RuntimeException("Error al crear la orden de compra", e);
	    }
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





	private String generarMensajeKafka(OrdenCompra orden) {
		// Crear un mensaje con los datos requeridos
		return String.format("CodigoTienda:%s, IdOrden:%s, Items:%s, FechaSolicitud:%s",
				orden.getTiendaId(),
				orden.getId(), 
				orden.getItemsOrdenCompra().toString(), 
				orden.getFechaSolicitud().toString());
	}
}
