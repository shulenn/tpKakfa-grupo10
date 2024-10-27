package com.tpKafka_grupo10.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpKafka_grupo10.event.StockUpdateEvent;

import java.util.Map;

import java.util.Map;

public class StockUpdateEventSerializer implements Serializer<StockUpdateEvent> {

	private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, StockUpdateEvent data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing StockUpdateEvent", e);
        }
    }

    @Override
    public void close() {
    }
}