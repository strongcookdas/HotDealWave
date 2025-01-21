package com.sparta.hotdeal.payment.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.port.OrderClientPort;
import com.sparta.hotdeal.payment.event.message.payment.ReqPaymentReadyMessage;
import com.sparta.hotdeal.payment.event.producer.PaymentDlqProducer;
import com.sparta.hotdeal.payment.event.producer.PaymentEventProducer;
import com.sparta.hotdeal.payment.event.service.PaymentConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentReadyEventConsumer {

    private final OrderClientPort orderClientPort;
    private final PaymentConsumerService paymentConsumerService;
    private final ObjectMapper objectMapper;
    private final PaymentEventProducer paymentEventProducer;
    private final PaymentDlqProducer paymentDlqProducer;

    @KafkaListener(topics = "${spring.kafka.topics.ready-payment}", groupId = "payment-group")
    public void consumeReadyPayment(String message) {
        try {
            ReqPaymentReadyMessage reqPaymentReadyMessage = parseMessage(message);
            OrderDto orderDto = orderClientPort.getOrderById(reqPaymentReadyMessage.getOrderId());
            paymentConsumerService.readyPayment(orderDto);
        } catch (JsonProcessingException e) {
            handleJsonProcessingError(message, e);
        } catch (Exception e) {
            handlePaymentError(message, e);
        }
    }

    private ReqPaymentReadyMessage parseMessage(String message) throws JsonProcessingException {
        try {
            log.info("message : {}", message);
            return objectMapper.readValue(message, ReqPaymentReadyMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON message: {}", message, e);
            throw e;
        }
    }

    private void handleJsonProcessingError(String message, JsonProcessingException e) {
        log.error("역직렬화 실패 오류 : {}. Error: {}", message, e.getMessage());
        paymentDlqProducer.sendToDeadLetterQueue(message, "JSON 파싱 실패: " + e.getMessage());
    }

    private void handlePaymentError(String message, Exception e) {
        try {
            log.error("결제 요청 실패 : {}", e.getMessage());
            ReqPaymentReadyMessage reqPaymentReadyMessage = objectMapper.readValue(message,
                    ReqPaymentReadyMessage.class);
            OrderDto orderDto = orderClientPort.getOrderById(reqPaymentReadyMessage.getOrderId());
            paymentEventProducer.sendRollbackMessage(orderDto);
        } catch (Exception recoveryException) {
            log.error("롤백 처리 (상품 수량 복구) 실패 오류 : {}. Recovery error: {}", e.getMessage(),
                    recoveryException.getMessage());
        }
    }
}
