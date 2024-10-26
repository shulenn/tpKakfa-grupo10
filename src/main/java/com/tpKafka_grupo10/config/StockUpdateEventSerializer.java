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
        // Configuraciones adicionales si son necesarias
    }

    @Override
    public byte[] serialize(String topic, StockUpdateEvent data) {
        if (data == null) {
            return null;
        }
        try {
            // Serializa el objeto StockUpdateEvent a un arreglo de bytes
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing StockUpdateEvent", e);
        }
    }

    @Override
    public void close() {
        // Aquí puedes liberar recursos si es necesario
    }
}