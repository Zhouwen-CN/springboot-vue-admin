package com.yeeiee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * <p>
 * jackson 配置
 * <pre>
 *     web的序列化和redis的序列化会冲突，各用各的
 * </pre>
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@Configuration
public class JacksonConfiguration {
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.createXmlMapper(false).build();
    }

    @Bean("redisObjectMapper")
    public ObjectMapper redisObjectMapper(Jackson2ObjectMapperBuilder builder) {
        val objectMapper =  builder.createXmlMapper(false).build();

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }
}