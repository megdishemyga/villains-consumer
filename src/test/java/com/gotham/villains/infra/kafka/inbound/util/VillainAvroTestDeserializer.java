package com.gotham.villains.infra.kafka.inbound.util;

import com.gotham.VillainEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class VillainAvroTestDeserializer implements Deserializer<Object> {

    @Override
    public Object deserialize(String s, byte[] bytes) {
        try {
            return VillainEvent.fromByteBuffer(ByteBuffer.wrap(bytes));
        } catch (IOException e) {
            return null;
        }
    }
}
