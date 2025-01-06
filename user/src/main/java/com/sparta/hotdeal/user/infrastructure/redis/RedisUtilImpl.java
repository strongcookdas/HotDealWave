package com.sparta.hotdeal.user.infrastructure.redis;

import com.sparta.hotdeal.user.application.util.RedisUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtilImpl implements RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setValues(String key, String value, long duration) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(duration));
    }

    @Override
    public String getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
