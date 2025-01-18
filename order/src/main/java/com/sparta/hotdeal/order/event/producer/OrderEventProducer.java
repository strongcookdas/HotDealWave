package com.sparta.hotdeal.order.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.event.message.payment.ReqPaymentCancelMessage;
import com.sparta.hotdeal.order.event.message.payment.ReqPaymentRefundMessage;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
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

    @Value("${spring.kafka.origins.create-order}")
    private String createOrderOrigin;
    @Value("${spring.kafka.origins.cancel-order}")
    private String cancelOrderOrigin;
    @Value("${spring.kafka.origins.refund-order}")
    private String refundOrderOrigin;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendReduceProductQuantityMessage(Order order, List<Basket> basketList) throws JsonProcessingException {
        try {
            ReqProductReduceQuantityDto req = ReqProductReduceQuantityDto.create(order.getId(), basketList);
            String message = objectMapper.writeValueAsString(req);
            sendMessage(createOrderOrigin, reduceProductQuantityTopic, req.getOrderId().toString(), message);
        } catch (Exception e) {
            throw e;
        }
    }

    public void sendCancelPaymentMessage(Order order) {
        try {
            ReqPaymentCancelMessage reqPaymentCancelMessage = ReqPaymentCancelMessage.of(order);
            String message = objectMapper.writeValueAsString(reqPaymentCancelMessage);
            sendMessage(cancelOrderOrigin, cancelPaymentTopic, order.getId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public void sendRefundPaymentMessage(Order order) {
        try {
            ReqPaymentRefundMessage reqPaymentRefundMessage = ReqPaymentRefundMessage.of(order);
            String message = objectMapper.writeValueAsString(reqPaymentRefundMessage);
            sendMessage(refundOrderOrigin, refundPaymentTopic, order.getId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    /*
    public void sendMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
    */

    private void sendMessage(String origin, String topic, String key, String payload) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(payload)
                        .setHeader("origin", origin) // 헤더에 origin 설정
                        .setHeader(KafkaHeaders.TOPIC, topic) // 토픽 설정
                        .build()
        );
    }
}
