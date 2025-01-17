package com.sparta.hotdeal.payment.event.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class PaymentDlqConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topics.dlq}", groupId = "dlq-group")
    public void listenDlqMessages(String message) {
        log.error("DLQ 메시지 수신: {}", message);
        try {
            Map<String, String> dlqMessage = objectMapper.readValue(message, new TypeReference<>() {
            });
            String originalMessage = dlqMessage.get("originalMessage");
            String errorMessage = dlqMessage.get("errorMessage");

            log.info("DLQ 메시지 분석 완료: 원본 메시지: {}, 에러 메시지: {}", originalMessage, errorMessage);

        } catch (Exception e) {
            log.error("DLQ 메시지 처리 실패: {}", e.getMessage());
        }
    }
}
