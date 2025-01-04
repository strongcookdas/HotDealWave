package com.sparta.hotdeal.user.application.redis;

public interface RedisUtil {
    void setValues(String key, String value, long duration);
    String getValues(String key);
    void deleteValues(String key);
}
