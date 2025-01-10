package com.sparta.hotdeal.order.infrastructure.config;

import com.sparta.hotdeal.order.infrastructure.exception.decoder.PaymentClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class PaymentClientConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new PaymentClientErrorDecoder();
    }
}
