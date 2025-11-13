package com.yeeiee.system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCacheConfiguration {
    /**
     * cache redis 配置
     *
     * @param cacheProperties cache配置
     * @return redis cache配置
     */
    @Bean
    public org.springframework.data.redis.cache.RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties, @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        // 序列化
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        org.springframework.data.redis.cache.RedisCacheConfiguration cacheConfig = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        // 先载入配置文件中的配置信息
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        // 过期时间设置
        if (redisProperties.getTimeToLive() != null) {
            cacheConfig = cacheConfig.entryTtl(redisProperties.getTimeToLive());
        }

        // KEY前缀配置
        if (redisProperties.getKeyPrefix() != null) {
            cacheConfig = cacheConfig.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }

        // 缓存空值配置
        if (!redisProperties.isCacheNullValues()) {
            cacheConfig = cacheConfig.disableCachingNullValues();
        }

        // 是否启用前缀
        if (!redisProperties.isUseKeyPrefix()) {
            cacheConfig = cacheConfig.disableKeyPrefix();
        }

        return cacheConfig;
    }
}