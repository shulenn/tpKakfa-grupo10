package com.tpKafka_grupo10.controller;


import com.tpKafka_grupo10.model.OrdenDespacho;
import com.tpKafka_grupo10.service.OrdenDespachoService; // Asegúrate de crear el servicio correspondiente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes-despacho")
public class OrdenDespachoController {

    @Autowired
    private OrdenDespachoService ordenDespachoService; // Asegúrate de implementar este servicio

    @PostMapping
    public ResponseEntity<OrdenDespacho> crearOrdenDespacho(@RequestBody OrdenDespacho ordenDespacho) {
        if (ordenDespacho.getProductos() == null || ordenDespacho.getProductos().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Manejar el error si no hay productos
        }
        OrdenDespacho nuevaOrden = ordenDespachoService.crearOrdenDespacho(ordenDespacho);
        return ResponseEntity.ok(nuevaOrden);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDespacho> obtenerOrdenDespacho(@PathVariable Long id) {
        OrdenDespacho orden = ordenDespachoService.obtenerOrdenDespacho(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping
    public ResponseEntity<List<OrdenDespacho>> listarOrdenesDespacho() {
        List<OrdenDespacho> ordenes = ordenDespachoService.listarOrdenesDespacho();
        return ResponseEntity.ok(ordenes);
    }
}
