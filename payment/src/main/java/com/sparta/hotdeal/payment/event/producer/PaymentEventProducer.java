package com.sparta.hotdeal.payment.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.event.message.order.ReqOrderUpdateStatusMessage;
import com.sparta.hotdeal.payment.common.exception.ApplicationException;
import com.sparta.hotdeal.payment.common.exception.ErrorCode;
import com.sparta.hotdeal.payment.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.event.message.product.ReqProductRecoverQuantityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    @Value("${spring.kafka.topics.update-order-status}")
    private String updateOrderStatusTopic;

    @Value("${spring.kafka.topics.rollback-reduce-quantity}")
    private String rollbackProductReduceQuantity;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendUpdateOrderStatusMessage(Payment payment, OrderStatus status) {
        try {
            ReqOrderUpdateStatusMessage reqOrderUpdateStatusMessage = ReqOrderUpdateStatusMessage.of(
                    payment.getOrderId(),
                    status);
            String message = objectMapper.writeValueAsString(reqOrderUpdateStatusMessage);
            sendMessage(updateOrderStatusTopic, payment.getOrderId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public void sendRollbackMessage(OrderDto orderDto) throws JsonProcessingException {
        ReqProductRecoverQuantityDto reqProductRecoverQuantityDto = ReqProductRecoverQuantityDto.of(orderDto);
        String message = objectMapper.writeValueAsString(reqProductRecoverQuantityDto);
        sendMessage(rollbackProductReduceQuantity, orderDto.getOrderId().toString(), message);
        log.info("상품 수량 감소 롤백 처리 Order ID: {}", orderDto.getOrderId());
    }

    public void sendMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
}
