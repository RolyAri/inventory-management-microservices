package com.rolydev.notification_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String toJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "parsed-events", key = "#json")
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
