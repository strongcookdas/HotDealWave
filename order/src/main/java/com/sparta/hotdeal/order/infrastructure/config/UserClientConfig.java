package com.sparta.hotdeal.order.infrastructure.config;

import com.sparta.hotdeal.order.infrastructure.exception.decoder.UserClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class UserClientConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserClientErrorDecoder();
    }
}
