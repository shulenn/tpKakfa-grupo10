package com.tpKafka_grupo10.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tpKafka_grupo10.model.Producto;
import com.tpKafka_grupo10.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Endpoint para crear un nuevo producto
    @PostMapping("/crear")
    public ResponseEntity<String> crearProducto(@RequestBody Producto producto) {
        try {
            productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el producto: " + e.getMessage());
        }
    }
}
