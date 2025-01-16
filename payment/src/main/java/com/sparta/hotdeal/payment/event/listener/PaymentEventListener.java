package com.sparta.hotdeal.payment.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.port.OrderClientPort;
import com.sparta.hotdeal.payment.application.service.PaymentService;
import com.sparta.hotdeal.payment.event.message.ReqProductRecoverQuantityDto;
import com.sparta.hotdeal.payment.event.message.ReqReadyPaymentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "[payment-event-listener]")
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OrderClientPort orderClientPort;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

//    @KafkaListener(topics = "ready-payment-topic")
//    public void consumeReadyPayment(String message) {
//        try {
//            ReqReadyPaymentMessage reqReadyPaymentMessage = parseMessage(message);
//
//            OrderDto orderDto = orderClientPort.getOrderById(reqReadyPaymentMessage.getOrderId());
//
//            paymentService.readyPayment(orderDto);
//        } catch (JsonProcessingException e) {
//            // JSON 파싱 오류 처리
//            handleJsonProcessingError(message, e);
//        } catch (Exception e) {
//            // 결제 또는 주문 관련 오류 처리
//            handlePaymentError(message, e);
//        }
//    }

    private ReqReadyPaymentMessage parseMessage(String message) throws JsonProcessingException {
        try {
            return objectMapper.readValue(message, ReqReadyPaymentMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON message: {}", message, e);
            throw e;
        }
    }

    private void handleJsonProcessingError(String message, JsonProcessingException e) {
        log.error("JSON parsing failed for message: {}. Error: {}", message, e.getMessage());
    }

    private void handlePaymentError(String message, Exception e) {
        try {
            log.error("Payment processing failed. Error: {}", e.getMessage());

            // JSON 메시지에서 복구 가능한 정보 추출
            ReqReadyPaymentMessage reqReadyPaymentMessage = objectMapper.readValue(message,
                    ReqReadyPaymentMessage.class);
            OrderDto orderDto = orderClientPort.getOrderById(reqReadyPaymentMessage.getOrderId());

            // 복구 요청 메시지 생성 및 발송
            sendRollbackMessage(orderDto);
        } catch (Exception recoveryException) {
            log.error("Rollback processing failed. Original error: {}. Recovery error: {}", e.getMessage(),
                    recoveryException.getMessage());
        }
    }

    private void sendRollbackMessage(OrderDto orderDto) throws JsonProcessingException {
        ReqProductRecoverQuantityDto reqProductRecoverQuantityDto = ReqProductRecoverQuantityDto.of(orderDto);
        String reqJson = objectMapper.writeValueAsString(reqProductRecoverQuantityDto);
        kafkaTemplate.send("rollback-reduce-quantity-topic", reqJson);
        log.info("Rollback message sent for Order ID: {}", orderDto.getOrderId());
    }

    //테스트용 주문 -> 결제 (상품 생략)
    @Transactional
    @KafkaListener(topics = "ready-payment-topic")
    public void consumeReadyPayment(String message) throws JsonProcessingException {
        try {
            ReqReadyPaymentMessage reqReadyPaymentMessage = parseMessage(message);

            OrderDto orderDto = orderClientPort.getOrderById(reqReadyPaymentMessage.getOrderId());
            log.info("주문 상세 조회 api 호출");

            paymentService.readyPayment(orderDto);
            log.info("결제 요청 api 호출");

        } catch (JsonProcessingException e) {
            // JSON 파싱 오류 처리
            handleJsonProcessingError(message, e);
        } catch (Exception e) {
            ReqReadyPaymentMessage reqReadyPaymentMessage = objectMapper.readValue(message,
                    ReqReadyPaymentMessage.class);
            String reqJson = objectMapper.writeValueAsString(reqReadyPaymentMessage);
            log.info("Rollback message sent for Order ID: {}", reqReadyPaymentMessage.getOrderId());
            log.info("exception : {}", e.getMessage());
            kafkaTemplate.send("cancel-order-topic", reqJson);
        }
    }
}
