package com.tpKafka_grupo10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<String> sendMessageToKafka(@RequestParam("topic")String topic ,@RequestParam("message") String message) {
		producerService.sendMessage(topic, message);
		return ResponseEntity.ok("Message sent to Kafka");
	}
}
