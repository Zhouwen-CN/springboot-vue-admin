package com.yeeiee.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

/**
 * <p>
 * json工具类
 * </p>
 *
 * @author chen
 * @since 2025-01-03
 */
public final class JsonUtil {
    public static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // 使用 JavaTimeModule 支持 Java 8 时间类型
        objectMapper.registerModule(new JavaTimeModule());

    }

    @SneakyThrows
    public static String toJsonString(Object value) {
        return objectMapper.writeValueAsString(value);
    }


    public static <T> T parse(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
}
