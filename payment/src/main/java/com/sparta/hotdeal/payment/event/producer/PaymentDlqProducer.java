package com.sparta.hotdeal.payment.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDlqProducer {

    @Value("${spring.kafka.topics.dlq}")
    private String paymentDlqTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToDeadLetterQueue(String originalMessage, String errorMessage) {
        try {
            // DLQ로 전송할 메시지 구성
            String dlqMessage = objectMapper.writeValueAsString(Map.of(
                    "originalMessage", originalMessage,
                    "errorMessage", errorMessage
            ));
            // DLQ 토픽으로 전송
            kafkaTemplate.send(paymentDlqTopic, dlqMessage);
            log.info("DLQ에 메시지 전송 완료: {}", dlqMessage);
        } catch (JsonProcessingException e) {
            log.error("DLQ 메시지 생성 실패: {}", e.getMessage());
        }
    }
}
