package com.sparta.hotdeal.coupon.infrastructure.config;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Value("${spring.data.redis.username:}") //기본값 빈 문자열로
    private String redisUsername;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort);
        if (!redisUsername.isBlank()) {
            config.useSingleServer()
                    .setClientName(redisUsername)
                    .setUsername(redisUsername);
        }
        if (!redisPassword.isBlank()) {
            config.useSingleServer().setPassword(redisPassword);
        }

        return org.redisson.Redisson.create(config);
    }
}
