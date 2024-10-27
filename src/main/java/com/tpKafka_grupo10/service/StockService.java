package com.tpKafka_grupo10.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.ItemOrdenDeCompra;
import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.model.Stock;
import com.tpKafka_grupo10.repository.OrdenCompraRepository;
import com.tpKafka_grupo10.repository.StockRepository;

import jakarta.transaction.Transactional;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    
    @Autowired
    private KafkaTemplate<String, StockUpdateEvent> kafkaTemplate;

    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public boolean proveedorProveeProducto(Long productoId) {
        return stockRepository.findByProductoCodigo(productoId) != null;
    }

    public boolean tieneStockSuficiente(Long productoId, int cantidad) {
        Stock stock = stockRepository.findByProductoCodigo(productoId);
        boolean tieneSuficiente = stock != null && stock.getCantidad() >= cantidad;
        if (!tieneSuficiente) {
            logger.warn("No hay suficiente stock para el producto: {}", productoId);
        }
        return tieneSuficiente;
    }

    @Transactional
    public void restarStock(Long productoId, int cantidad) {
        Stock stock = stockRepository.findByProductoCodigo(productoId);
        if (stock != null) {
            if (stock.getCantidad() >= cantidad) {
                stock.setCantidad(stock.getCantidad() - cantidad);
                stockRepository.save(stock); // Guardar los cambios en la base de datos
                logger.info("Se ha restado {} unidades del producto: {}", cantidad, productoId);
            } else {
                logger.error("Intento de restar más stock del disponible para el producto: {}", productoId);
                throw new IllegalArgumentException("No hay suficiente stock para restar.");
            }
        } else {
            logger.error("Producto no encontrado en stock: {}", productoId);
            throw new IllegalArgumentException("Producto no encontrado en stock.");
        }
    } 
    
    public void actualizarStock(Long productoId, int nuevaCantidad) {
        // Actualizar la cantidad de stock para el producto especificado
        Stock stock = stockRepository.findByProductoCodigo(productoId);
        if (stock != null) {
            stock.setCantidad(nuevaCantidad);
            stockRepository.save(stock);

            // Enviar el evento de actualización de stock a Kafka
            StockUpdateEvent event = new StockUpdateEvent();
            event.setProductoId(productoId);
            event.setNuevaCantidad(nuevaCantidad);
            kafkaTemplate.send("stock-actualizado", event);
            
            // Reprocesar órdenes pausadas
            List<OrdenCompra> ordenesPausadas = ordenCompraRepository.findOrdenesPausadasPorProducto(productoId);
            for (OrdenCompra orden : ordenesPausadas) {
                if (puedeCumplirOrden(orden)) {
                    orden.setEstado(EstadoOrden.ACEPTADA);
                    orden.setPausada(false);
                    ordenCompraRepository.save(orden);
                }
            }
        } else {
            throw new IllegalArgumentException("Producto no encontrado en el stock.");
        }
    }

    private boolean puedeCumplirOrden(OrdenCompra orden) {
        // Lógica para verificar si la orden se puede cumplir con el stock actualizado
        for (ItemOrdenDeCompra item : orden.getItemsOrdenCompra()) {
            Stock stock = stockRepository.findByProductoCodigo(item.getProducto().getCodigo());
            if (stock == null || stock.getCantidad() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }
}
