package com.sparta.hotdeal.order.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.event.message.order.ResOrderCancelMessage;
import com.sparta.hotdeal.order.event.producer.OrderDlqProducer;
import com.sparta.hotdeal.order.event.service.OrderConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCancelEventConsumer {


    private final OrderConsumerService orderConsumerService;
    private final OrderDlqProducer orderDlqProducer;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.cancel-order}", groupId = "order-group")
    public void consumeCancelOrder(String message) {
        try {
            ResOrderCancelMessage resMessage = parseMessage(message);
            orderConsumerService.cancelOrder(resMessage.getOrderId());
        } catch (JsonProcessingException e) {
            handleJsonProcessingError(message, e);
        } catch (Exception e) {
            handleOrderCancelUpdateError(message, e);
        }
    }

    private ResOrderCancelMessage parseMessage(String message) throws JsonProcessingException {
        try {
            log.info("message : {}", message);
            return objectMapper.readValue(message, ResOrderCancelMessage.class);

        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패 오류 : {}", message, e);
            throw e;
        }
    }

    private void handleJsonProcessingError(String message, JsonProcessingException e) {
        log.error("역직렬화 실패 오류 : {}. Error: {}", message, e.getMessage());
        orderDlqProducer.sendToDeadLetterQueue(message, "JSON 파싱 실패: " + e.getMessage());
    }

    private void handleOrderCancelUpdateError(String message, Exception e) {
        log.error("주문 취소 오류 : {}. Error: {}", message, e.getMessage());
        orderDlqProducer.sendToDeadLetterQueue(message, "주문 취소 실패: " + e.getMessage());
    }
}
