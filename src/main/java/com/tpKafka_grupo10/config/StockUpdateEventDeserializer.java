package com.tpKafka_grupo10.config;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpKafka_grupo10.event.StockUpdateEvent;

public class StockUpdateEventDeserializer implements Deserializer<StockUpdateEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public StockUpdateEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, StockUpdateEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializando StockUpdateEvent", e);
        }
    }
}