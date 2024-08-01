package com.avanza.license.config;

import com.avanza.license.Dto.UserLicenseFloatAbleDTO;
import com.avanza.license.entity.UserLicense;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

public class CustomRedisSerializer implements RedisSerializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (IOException e) {
            throw new SerializationException("Error serializing object", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            return objectMapper.readValue(bytes, UserLicenseFloatAbleDTO.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing object", e);
        }
    }
}
