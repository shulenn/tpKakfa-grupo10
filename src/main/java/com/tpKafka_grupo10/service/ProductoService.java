package com.tpKafka_grupo10.service;

import com.tpKafka_grupo10.event.ProductoNovedadEvent;
import com.tpKafka_grupo10.model.Producto;
import com.tpKafka_grupo10.model.ProductoAltaDetalle;
import com.tpKafka_grupo10.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private KafkaTemplate<String, ProductoNovedadEvent> kafkaTemplateNovedad;

    // Método para enviar un producto como novedad
    public void crearProducto(Producto producto) {
        productoRepository.save(producto);

        // Enviar evento de novedad a Kafka
        ProductoNovedadEvent novedadEvent = new ProductoNovedadEvent();
        novedadEvent.setCodigoProducto(producto.getCodigo());
        kafkaTemplateNovedad.send("novedades", novedadEvent);
    }

    // Nuevo método para dar de alta talles y colores específicos de un producto
    public void darDeAltaProductoConDetalle(ProductoAltaDetalle detalle) {
        // Recupera el producto por su código
        Producto producto = productoRepository.findByCodigo(detalle.getCodigoProducto());

        if (producto != null) {
            // Recorre el mapa de talles y colores seleccionados
            for (Map.Entry<String, List<String>> entrada : detalle.getTallesYColoresSeleccionados().entrySet()) {
                String talle = entrada.getKey();
                List<String> colores = entrada.getValue();

                // Realiza el alta de cada combinación de talle y color seleccionada
                for (String color : colores) {
                    Producto varianteProducto = new Producto();
                    varianteProducto.setCodigo(producto.getCodigo());
                    varianteProducto.setNombre(producto.getNombre());
                    varianteProducto.setTalle(talle);
                    varianteProducto.setColor(color);

                    // Guarda cada variante en la base de datos
                    productoRepository.save(varianteProducto);
                }
            }
        }
    }
    
  
}
