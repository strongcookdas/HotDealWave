package com.sparta.hotdeal.order.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.event.message.payment.ReqPaymentCancelMessage;
import com.sparta.hotdeal.order.event.message.payment.ReqPaymentRefundMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    @Value("${spring.kafka.topics.request-product}")
    private String reduceProductQuantityTopic;
    @Value("${spring.kafka.topics.cancel-payment}")
    private String cancelPaymentTopic;
    @Value("${spring.kafka.topics.refund-payment}")
    private String refundPaymentTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendReduceProductQuantityMessage(Order order, List<Basket> basketList) throws JsonProcessingException {
        ReqProductReduceQuantityDto req = ReqProductReduceQuantityDto.create(order.getId(), basketList);
        String message = objectMapper.writeValueAsString(req);
        sendMessage(reduceProductQuantityTopic, req.getOrderId().toString(), message);
    }

    public void sendCancelPaymentMessage(Order order) {
        try {
            ReqPaymentCancelMessage reqPaymentCancelMessage = ReqPaymentCancelMessage.of(order);
            String message = objectMapper.writeValueAsString(reqPaymentCancelMessage);
            sendMessage(cancelPaymentTopic, order.getId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public void sendRefundPaymentMessage(Order order) {
        try {
            ReqPaymentRefundMessage reqPaymentRefundMessage = ReqPaymentRefundMessage.of(order);
            String message = objectMapper.writeValueAsString(reqPaymentRefundMessage);
            sendMessage(refundPaymentTopic, order.getId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }


    private void sendMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
}
