package com.yeeiee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // value序列化方式采用jackson
        val valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(valueSerializer);

        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        //hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(valueSerializer);

        return redisTemplate;
    }
}
