package com.sparta.hotdeal.order.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.event.message.order.ResOrderCancelMessage;
import com.sparta.hotdeal.order.event.message.order.ResOrderUpdateStatusMessage;
import com.sparta.hotdeal.order.event.service.OrderConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {


    private final OrderConsumerService orderConsumerService;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.cancel-order}", groupId = "order-group")
    public void consumeCancelOrder(String message) {
        try {
            ResOrderCancelMessage resMessage = objectMapper.readValue(message, ResOrderCancelMessage.class);
            orderConsumerService.cancelOrder(resMessage.getOrderId());
        } catch (Exception e) {
            // 주문 취소에서 에러나면 DLQ (Dead Letter Queue) 처리가 필요하지만 일단 log 찍는 것으로 대체
            log.error("error message : {}", e.getMessage());
        }
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.update-order-status}", groupId = "order-group")
    public void consumeUpdateOrderStatus(String message) {
        try {
            ResOrderUpdateStatusMessage resMessage = objectMapper.readValue(message, ResOrderUpdateStatusMessage.class);
            orderConsumerService.updateOrderStatus(resMessage.getOrderId(), resMessage.getStatus());
        } catch (Exception e) {
            // 주문 취소에서 에러나면 DLQ (Dead Letter Queue) 처리가 필요하지만 일단 log 찍는 것으로 대체
            log.error("error message : {}", e.getMessage());
        }
    }
}
