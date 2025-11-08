package org.example.basespringbootproject.infrastructure.aop.logging;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class LoggingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return String.valueOf(obj);
        }
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
