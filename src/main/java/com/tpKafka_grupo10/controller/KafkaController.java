package com.tpKafka_grupo10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpKafka_grupo10.event.StockUpdateEvent;
import com.tpKafka_grupo10.kafka.producer.ProducerService;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final ProducerService producerService;

    @Autowired
    public KafkaController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> sendMessageToKafka(@RequestBody StockUpdateEvent stockUpdateEvent) {
        producerService.enviarEvento(stockUpdateEvent);
        return ResponseEntity.ok("Stock update event sent to Kafka");
    }
}

