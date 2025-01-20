package com.sparta.hotdeal.payment.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.port.OrderClientPort;
import com.sparta.hotdeal.payment.application.service.PaymentService;
import com.sparta.hotdeal.payment.event.message.ReqPaymentCancelMessage;
import com.sparta.hotdeal.payment.event.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "[payment-event-listener]")
@Component
@RequiredArgsConstructor
public class PaymentRefundEventConsumer {

    private final OrderClientPort orderClientPort;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(topics = "${spring.kafka.topics.refund-payment}", groupId = "payment-group")
    public void consumeReadyPayment(String message) {
        try {
            ReqPaymentCancelMessage reqPaymentCancelMessage = parseMessage(message);
            OrderDto orderDto = orderClientPort.getOrderById(reqPaymentCancelMessage.getOrderId());
            paymentService.refundPayment(orderDto.getUserId(), orderDto.getOrderId());
        } catch (JsonProcessingException e) {
            handleJsonProcessingError(message, e);
        } catch (Exception e) {
            handlePaymentCancelError(message, e);
        }
    }

    private ReqPaymentCancelMessage parseMessage(String message) throws JsonProcessingException {
        try {
            log.info("message : {}", message);
            return objectMapper.readValue(message, ReqPaymentCancelMessage.class);

        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패 오류 : {}", message, e);
            throw e;
        }
    }

    private void handleJsonProcessingError(String message, JsonProcessingException e) {
        log.error("역직렬화 실패 오류 : {}. Error: {}", message, e.getMessage());
        paymentEventProducer.sendToDeadLetterQueue(message, "JSON 파싱 실패: " + e.getMessage());
    }

    private void handlePaymentCancelError(String message, Exception e) {
        log.error("결제 환불 오류 : {}. Error: {}", message, e.getMessage());
        paymentEventProducer.sendToDeadLetterQueue(message, "결제 환불 실패: " + e.getMessage());
    }
}
