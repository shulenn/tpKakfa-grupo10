package com.tpKafka_grupo10.listener;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.repository.OrdenCompraRepository; // Asegúrate de importar el repositorio necesario
import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.OrdenCompra; // Importa la clase de modelo correcta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockEventListener {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @KafkaListener(topics = "stock-actualizado", groupId = "grupo-proveedor")
    public void manejarActualizacionStock(StockUpdateEvent event) {
        List<OrdenCompra> ordenesPausadas = ordenCompraRepository.findOrdenesPausadasPorProducto(event.getProductoId());
        for (OrdenCompra orden : ordenesPausadas) {
            if (puedeCumplirOrden(orden)) {  // Asegúrate de que el método puedaCumplirOrden esté definido
                orden.setEstado(EstadoOrden.ACEPTADA);
                orden.setPausada(false);
                ordenCompraRepository.save(orden);
            }
        }
    }

    private boolean puedeCumplirOrden(OrdenCompra orden) {
        // Implementa tu lógica para verificar si se puede cumplir la orden
        return true; // O la lógica que corresponda
    }
}

