package com.gotham.villains.infra.kafka.inbound.util;

import com.gotham.VillainEvent;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;


public class VillainAvroTestSerializer implements Serializer<Object> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, Object o) {
        if (o instanceof VillainEvent event) {
            try {
                return event.toByteBuffer().array();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new byte[0];
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        return Serializer.super.serialize(topic, headers, data);
    }
}
