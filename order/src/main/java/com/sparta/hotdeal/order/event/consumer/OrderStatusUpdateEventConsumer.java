package com.sparta.hotdeal.order.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.event.message.order.ResOrderUpdateStatusMessage;
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
public class OrderStatusUpdateEventConsumer {


    private final OrderConsumerService orderConsumerService;
    private final OrderDlqProducer orderDlqProducer;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.update-order-status}", groupId = "order-group")
    public void consumeUpdateOrderStatus(String message) {
        try {
            ResOrderUpdateStatusMessage resMessage = parseMessage(message);
            orderConsumerService.updateOrderStatus(resMessage.getOrderId(), resMessage.getStatus());
        } catch (JsonProcessingException e) {
            handleJsonProcessingError(message, e);
        } catch (Exception e) {
            handleOrderStatusUpdateError(message, e);
        }
    }

    private ResOrderUpdateStatusMessage parseMessage(String message) throws JsonProcessingException {
        try {
            log.info("message : {}", message);
            return objectMapper.readValue(message, ResOrderUpdateStatusMessage.class);

        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패 오류 : {}", message, e);
            throw e;
        }
    }

    private void handleJsonProcessingError(String message, JsonProcessingException e) {
        log.error("역직렬화 실패 오류 : {}. Error: {}", message, e.getMessage());
        orderDlqProducer.sendToDeadLetterQueue(message, "JSON 파싱 실패: " + e.getMessage());
    }

    private void handleOrderStatusUpdateError(String message, Exception e) {
        log.error("주문 상태 업데이트 오류 : {}. Error: {}", message, e.getMessage());
        orderDlqProducer.sendToDeadLetterQueue(message, "주문 상태 업데이트 실패: " + e.getMessage());
    }

}
