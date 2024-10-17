package com.tpKafka_grupo10.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.model.Stock;
import com.tpKafka_grupo10.repository.StockRepository;

@Service
public class StockService {

	@Autowired
	private StockRepository stockRepository;

	public boolean proveedorProveeProducto(Long productoId) {
        // Lógica para verificar si el proveedor puede proveer el producto
        return stockRepository.findByProductoCodigo(productoId) != null;
    }

    public boolean tieneStockSuficiente(Long productoId, int cantidad) {
        Stock stock = stockRepository.findByProductoCodigo(productoId);
        return stock != null && stock.getCantidad() >= cantidad;
    }

    public void restarStock(Long productoId, int cantidad) {
        Stock stock = stockRepository.findByProductoCodigo(productoId);
        if (stock != null) {
            stock.setCantidad(stock.getCantidad() - cantidad);
            stockRepository.save(stock); // Guardar los cambios en la base de datos
        }
    }

}
