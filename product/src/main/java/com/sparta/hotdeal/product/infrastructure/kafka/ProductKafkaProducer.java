package com.sparta.hotdeal.product.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRollbackMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }

    public void sendPaymentMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
}
