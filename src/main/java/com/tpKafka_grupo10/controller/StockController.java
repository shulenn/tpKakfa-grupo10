package com.tpKafka_grupo10.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.service.StockService;

@RestController
@RequestMapping("/stock")
public class StockController {
	
	@Autowired
    private StockService stockService;

	/*@PostMapping("/actualizar/{productoId}")
    public ResponseEntity<String> actualizarStock(@RequestBody Map<String, Object> request) {
        try {
            Long productoId = Long.valueOf(request.get("productoId").toString());
            int cantidad = Integer.parseInt(request.get("cantidad").toString());

            stockService.actualizarStock(productoId, cantidad);

            return ResponseEntity.ok("Stock actualizado y órdenes reprocesadas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    */
	@PostMapping("/actualizar/{productoId}")
	public ResponseEntity<String> actualizarStock(@PathVariable Long productoId, @RequestParam int nuevaCantidad) {
        try {
            stockService.actualizarStock(productoId, nuevaCantidad);
            return ResponseEntity.ok("Stock actualizado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
	
	@PutMapping("/actualizar")
    public ResponseEntity<Void> actualizarStock(@RequestBody StockUpdateEvent request) {
        stockService.actualizarStock(request.getProductoId(), request.getNuevaCantidad());
        return ResponseEntity.ok().build();
    }
}
