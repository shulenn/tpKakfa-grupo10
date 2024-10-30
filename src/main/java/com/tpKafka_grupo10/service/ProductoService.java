package com.tpKafka_grupo10.service;

import com.tpKafka_grupo10.event.ProductoNovedadEvent;
import com.tpKafka_grupo10.model.Producto;
import com.tpKafka_grupo10.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private KafkaTemplate<String, ProductoNovedadEvent> kafkaTemplateNovedad;

    public void crearProducto(Producto producto) {
        // Guardar el producto en la base de datos
        productoRepository.save(producto);

        // Preparar el evento de novedad
        ProductoNovedadEvent novedadEvent = new ProductoNovedadEvent();
        novedadEvent.setCodigoProducto(producto.getCodigo());

        // Generar el mapa de talles y colores
        Map<String, List<String>> tallesYColores = new HashMap<>();
        tallesYColores.computeIfAbsent(producto.getTalle(), k -> new ArrayList<>()).add(producto.getColor());
        novedadEvent.setTallesYColores(tallesYColores);

        // Generar lista de URLs para las fotos
        List<String> fotosUrls = new ArrayList<>();
        fotosUrls.add(producto.getFoto());
        novedadEvent.setFotosUrls(fotosUrls);

        // Enviar evento a Kafka
        kafkaTemplateNovedad.send("novedades", novedadEvent);
    }
}
