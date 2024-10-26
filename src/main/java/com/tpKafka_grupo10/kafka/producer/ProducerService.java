package com.tpKafka_grupo10.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tpKafka_grupo10.event.StockUpdateEvent;

@Service
public class ProducerService {
	private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    private final KafkaTemplate<String, StockUpdateEvent> kafkaTemplate;

    @Autowired
    public ProducerService(KafkaTemplate<String, StockUpdateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Método para enviar mensajes al Kafka
    public void enviarEvento(StockUpdateEvent event) {
        kafkaTemplate.send("stock-actualizado", event);
    }

}
