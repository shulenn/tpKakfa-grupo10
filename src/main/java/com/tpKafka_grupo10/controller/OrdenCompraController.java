package com.tpKafka_grupo10.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.service.OrdenCompraService;

@RestController
@RequestMapping("/ordenes-compra")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraService ordenCompraService;

    @PostMapping
    public ResponseEntity<?> crearOrdenCompra(@RequestBody OrdenCompra ordenCompra) {
    	// Validar si la orden de compra es nula o no tiene productos
        if (ordenCompra == null || ordenCompra.getItemsOrdenCompra() == null || ordenCompra.getItemsOrdenCompra().isEmpty()) {
            return ResponseEntity.badRequest().body("La orden de compra no puede ser nula y debe tener al menos un ítem.");
        }

        try {
            OrdenCompra nuevaOrden = ordenCompraService.crearOrdenCompra(ordenCompra);
            return ResponseEntity.ok(nuevaOrden);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error al crear la orden de compra", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la orden de compra.");
        }
    }

    // Agrega métodos para modificar y eliminar órdenes de compra según sea necesario
}
