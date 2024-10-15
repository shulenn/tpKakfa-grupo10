package com.tpKafka_grupo10.controller;

import com.tpKafka_grupo10.model.ItemDespacho;
import com.tpKafka_grupo10.service.ItemDespachoService; // Asegúrate de crear el servicio correspondiente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items-despacho")
public class ItemDespachoController {

    @Autowired
    private ItemDespachoService itemDespachoService; // Asegúrate de implementar este servicio

    @PostMapping
    public ResponseEntity<ItemDespacho> crearItemDespacho(@RequestBody ItemDespacho itemDespacho) {
        ItemDespacho nuevoItem = itemDespachoService.crearItemDespacho(itemDespacho);
        return ResponseEntity.ok(nuevoItem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDespacho> obtenerItemDespacho(@PathVariable Long id) {
        ItemDespacho item = itemDespachoService.obtenerItemDespacho(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<ItemDespacho>> listarItemsDespacho() {
        List<ItemDespacho> items = itemDespachoService.listarItemsDespacho();
        return ResponseEntity.ok(items);
    }
}
