package com.sparta.hotdeal.user.application.util;

public interface RedisUtil {
    void setValues(String key, String value, long duration);
    String getValues(String key);
    void deleteValues(String key);
}
