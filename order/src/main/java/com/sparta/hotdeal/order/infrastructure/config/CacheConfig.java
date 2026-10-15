package com.sparta.hotdeal.order.infrastructure.config;

import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

  // 성능 테스트에서 캐싱 on/off 비교를 위해 spring.cache.type=none 이면 이 빈이 비활성화되고
  // Spring Boot의 NoOpCacheManager가 대신 등록됨(코드 수정 없이 환경변수만으로 토글).
  @Bean
  @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
  public RedisCacheManager cacheManager(
      RedisConnectionFactory redisConnectionFactory
  ) {
    RedisCacheConfiguration configuration = RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(10))
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(
            SerializationPair.fromSerializer(RedisSerializer.java())
        );

    return RedisCacheManager
        .builder(redisConnectionFactory)
        .cacheDefaults(configuration)
        .build();
  }
}
