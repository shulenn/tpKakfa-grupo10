package com.tpKafka_grupo10.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.ItemDespacho;
import com.tpKafka_grupo10.model.ItemOrdenDeCompra;
import com.tpKafka_grupo10.model.OrdenCompra;
import com.tpKafka_grupo10.model.OrdenDespacho;
import com.tpKafka_grupo10.model.Producto;
import com.tpKafka_grupo10.model.Stock;
import com.tpKafka_grupo10.repository.ItemDespachoRepository;
import com.tpKafka_grupo10.repository.OrdenCompraRepository;
import com.tpKafka_grupo10.repository.OrdenDespachoRepository;
import com.tpKafka_grupo10.repository.StockRepository;

import jakarta.transaction.Transactional;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;
    
    @Autowired
    private OrdenDespachoRepository ordenDespachoRepository;
    
    @Autowired
    private ItemDespachoRepository itemDespachoRepository;
    
    @Autowired
    private KafkaTemplate<String, StockUpdateEvent> kafkaTemplateEvent;
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplateString;
    
    
	public StockService(KafkaTemplate<String, String> kafkaTemplateString,
			KafkaTemplate<String, StockUpdateEvent> kafkaTemplateEvent) {
		this.kafkaTemplateString = kafkaTemplateString;
		this.kafkaTemplateEvent = kafkaTemplateEvent;
	}

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
        	int stockRestante = stock.getCantidad();
            stock.setCantidad(stockRestante + nuevaCantidad);
            stockRepository.save(stock);

            // Enviar el evento de actualización de stock a Kafka
            StockUpdateEvent event = new StockUpdateEvent();
            event.setProductoId(productoId);
            event.setNuevaCantidad(nuevaCantidad + stockRestante);
            kafkaTemplateEvent.send("stock-actualizado", event);

            // Reprocesar órdenes pausadas que contengan este producto
            List<OrdenCompra> ordenesPausadas = ordenCompraRepository.findOrdenesPausadasPorProducto(productoId);
            for (OrdenCompra orden : ordenesPausadas) {
                if (puedeCumplirOrden(orden)) {
                    // Despausar Orden
                    orden.setPausada(false);
                    ordenCompraRepository.save(orden);

                    // Generar orden de despacho
                    OrdenDespacho ordenDespacho = new OrdenDespacho();
                    ordenDespacho.setIdOrdenCompra(orden.getCodigo());
                    ordenDespacho.setFechaEstimadaEnvio(LocalDate.now().plusDays(2)); // Estimación de 2 días

                    // Guardar orden de despacho en la base de datos
                    ordenDespacho = ordenDespachoRepository.save(ordenDespacho);

                    // Crear los items de despacho basados en los items de la orden de compra
                    for (ItemOrdenDeCompra itemOrden : orden.getItemsOrdenCompra()) {
                        ItemDespacho itemDespacho = new ItemDespacho();

                        // Asignar el producto y la cantidad al item de despacho
                        Producto producto = itemOrden.getProducto();
                        itemDespacho.setProducto(producto);
                        itemDespacho.setCantidad(itemOrden.getCantidad());
                        itemDespacho.setOrdenDespacho(ordenDespacho);

                        // Guardar el item de despacho en la base de datos
                        itemDespachoRepository.save(itemDespacho);
                    }

                    // Enviar la orden de despacho a Kafka
                    kafkaTemplateString.send(orden.getTiendaId() + "-despacho", ordenDespacho.toString());

                    // Restar stock del proveedor para cada item
                    for (ItemOrdenDeCompra item : orden.getItemsOrdenCompra()) {
                        restarStock(item.getProducto().getCodigo(), item.getCantidad());
                    }
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
