package com.tpKafka_grupo10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.service.ProveedorService;

@RestController
@RequestMapping("/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Endpoint para procesar una orden de compra
    @PostMapping("/procesar-orden")
    public ResponseEntity<String> procesarOrden(@RequestBody OrdenCompra ordenCompra) {
        try {
            proveedorService.procesarOrdenCompra(ordenCompra);
            return ResponseEntity.ok("La orden fue procesada correctamente.");
        } catch (Exception e) {
            // Manejo de errores genéricos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al procesar la orden: " + e.getMessage());
        }
    }
}
