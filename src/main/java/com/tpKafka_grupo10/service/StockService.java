package com.tpKafka_grupo10.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class StockService {

	// Simulación de un repositorio de productos
	private Map<String, Integer> stock = new HashMap<>();

	public boolean proveedorProveeProducto(Long productoId) {
		// Lógica para verificar si el proveedor puede proveer el producto
		return stock.containsKey(productoId.toString());
	}

	public boolean tieneStockSuficiente(Long productoId, int cantidad) {
		return stock.getOrDefault(productoId, 0) >= cantidad;
	}

	public void restarStock(Long productoId, int cantidad) {
		stock.put(productoId.toString(), stock.get(productoId.toString()) - cantidad);
	}

}
