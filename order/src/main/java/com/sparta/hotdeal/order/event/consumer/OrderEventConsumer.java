package com.sparta.hotdeal.order.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPutOrderDto;
import com.sparta.hotdeal.order.application.service.order.OrderService;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.order.event.message.ReqOrderCancelMessage;
import com.sparta.hotdeal.order.event.message.ReqOrderUpdateStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {


    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.cancel-order}", groupId = "order-group")
    public void consumeCancelOrder(String message) {
        try {
            ReqOrderCancelMessage reqOrderCancelMessage = objectMapper.readValue(message, ReqOrderCancelMessage.class);
            orderService.cancelOrderByIdForMessage(reqOrderCancelMessage.getOrderId());
        } catch (Exception e) {
            // 주문 취소에서 에러나면 DLQ (Dead Letter Queue) 처리가 필요하지만 일단 log 찍는 것으로 대체
            log.error("error message : {}", e.getMessage());
        }
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.update-order-status}", groupId = "order-group")
    public void consumeUpdateOrderStatus(String message) {
        try {
            ReqOrderUpdateStatusMessage reqOrderUpdateStatusMessage = objectMapper.readValue(message,
                    ReqOrderUpdateStatusMessage.class);
            ReqPutOrderDto reqPutOrderDto = ReqPutOrderDto.of(
                    OrderStatus.valueOf(reqOrderUpdateStatusMessage.getStatus()));
            orderService.updateOrderStatus(reqOrderUpdateStatusMessage.getOrderId(), reqPutOrderDto);
        } catch (Exception e) {
            // 주문 취소에서 에러나면 DLQ (Dead Letter Queue) 처리가 필요하지만 일단 log 찍는 것으로 대체
            log.error("error message : {}", e.getMessage());
        }
    }
}
