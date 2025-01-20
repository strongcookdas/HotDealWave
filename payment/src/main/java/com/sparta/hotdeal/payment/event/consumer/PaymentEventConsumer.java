package com.sparta.hotdeal.payment.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.port.OrderClientPort;
import com.sparta.hotdeal.payment.application.service.PaymentService;
import com.sparta.hotdeal.payment.event.message.ReqProductRecoverQuantityDto;
import com.sparta.hotdeal.payment.event.message.ReqReadyPaymentMessage;
import com.sparta.hotdeal.payment.event.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[payment-event-listener]")
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    @Value("${spring.kafka.topics.rollback-reduce-quantity}")
    private String rollbackProductReduceQuantity;

    private final OrderClientPort orderClientPort;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(topics = "${spring.kafka.topics.ready-payment}", groupId = "payment-group")
    public void consumeReadyPayment(String message) {
        try {
            ReqReadyPaymentMessage reqReadyPaymentMessage = parseMessage(message);
            OrderDto orderDto = orderClientPort.getOrderById(reqReadyPaymentMessage.getOrderId());
            paymentService.readyPayment(orderDto);
        } catch (JsonProcessingException e) {
            // JSON 파싱 오류 처리
            handleJsonProcessingError(message, e);
        } catch (Exception e) {
            // 결제 또는 주문 관련 오류 처리
            handlePaymentError(message, e);
        }
    }

    private ReqReadyPaymentMessage parseMessage(String message) throws JsonProcessingException {
        try {
            log.info("message : {}", message);
            return objectMapper.readValue(message, ReqReadyPaymentMessage.class);

        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON message: {}", message, e);
            throw e;
        }
    }

    private void handleJsonProcessingError(String message, JsonProcessingException e) {
        log.error("역직렬화 실패 오류 : {}. Error: {}", message, e.getMessage());
        paymentEventProducer.sendToDeadLetterQueue(message, "JSON 파싱 실패: " + e.getMessage());
    }

    private void handlePaymentError(String message, Exception e) {
        try {
            log.error("결제 요청 실패 : {}", e.getMessage());

            ReqReadyPaymentMessage reqReadyPaymentMessage = objectMapper.readValue(message,
                    ReqReadyPaymentMessage.class);
            OrderDto orderDto = orderClientPort.getOrderById(reqReadyPaymentMessage.getOrderId());

            sendRollbackMessage(orderDto);
        } catch (Exception recoveryException) {
            log.error("롤백 처리 (상품 수량 복구) 실패 오류 : {}. Recovery error: {}", e.getMessage(),
                    recoveryException.getMessage());
        }
    }

    private void sendRollbackMessage(OrderDto orderDto) throws JsonProcessingException {
        ReqProductRecoverQuantityDto reqProductRecoverQuantityDto = ReqProductRecoverQuantityDto.of(orderDto);
        String message = objectMapper.writeValueAsString(reqProductRecoverQuantityDto);
        paymentEventProducer.sendMessage(rollbackProductReduceQuantity, orderDto.getOrderId().toString(), message);
        log.info("상품 수량 감소 롤백 처리 Order ID: {}", orderDto.getOrderId());
    }
}
