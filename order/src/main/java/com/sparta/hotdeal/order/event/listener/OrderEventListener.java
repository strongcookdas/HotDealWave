package com.sparta.hotdeal.order.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.service.order.OrderService;
import com.sparta.hotdeal.order.event.message.ReqOrderCancelMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "cancel-order-topic")
    public void consumeCancelOrder(String message) {
        try {
            ReqOrderCancelMessage reqOrderCancelMessage = objectMapper.readValue(message, ReqOrderCancelMessage.class);
            orderService.cancelOrderById(reqOrderCancelMessage.getOrderId());
        } catch (Exception e) {
            // 주문 취소에서 에러나면 DLQ (Dead Letter Queue) 처리가 필요하지만 일단 log 찍는 것으로 대체
            log.error("error message : {}", e.getMessage());
        }
    }
}
