package com.sparta.hotdeal.product.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductQuantityDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutProductQuantityDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.product.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductKafkaConsumer {

    private final ProductInventoryService productInventoryService;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.request-order}")
    private String requestOrderTopic;

    @KafkaListener(topics = "${spring.kafka.topics.reduce-quantity}", groupId = "product-group")
    @Transactional
    public void consumeReduceQuantity(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        // 메시지 역직렬화
        ReqPutProductQuantityDto requestDto =
                objectMapper.readValue(message, ReqPutProductQuantityDto.class);
        ResPutProductQuantityDto key = ResPutProductQuantityDto.of(requestDto.getOrderId());
        try {
            // 재고 차감 요청 처리
            productInventoryService.reduceQuantity(requestDto);
            acknowledgment.acknowledge(); // 메시지 처리 성공 후 명시적으로 커밋

            String readyPaymentMessage = objectMapper.writeValueAsString(key);
            productInventoryService.sendPaymentRequest(key.getOrderId().toString(), readyPaymentMessage);

            log.info("결제 요청 메시지 전송 완료 key : {}, message: {}", key.getOrderId().toString(), readyPaymentMessage);
        } catch (ApplicationException e) {
            log.error("재고 차감 처리 실패: {}", e.getMessage());
            String rollbackMessage = objectMapper.writeValueAsString(key);
            sendRollbackMessage(requestOrderTopic, key.getOrderId().toString(),
                    rollbackMessage); // 실패 시 전체 요청 롤백
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("예기치 않은 오류 발생: {}", e.getMessage());
            acknowledgment.acknowledge(); // 예외 발생 시에도 메시지 중복 처리를 방지
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.restore-quantity}", groupId = "product-group")
    @Transactional
    public void consumeRestoreQuantity(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        // 메시지 역직렬화
        ReqPutProductQuantityDto requestDto =
                objectMapper.readValue(message, ReqPutProductQuantityDto.class);
        ResPutProductQuantityDto key = ResPutProductQuantityDto.of(requestDto.getOrderId());
        try {
            // 재고 복구 요청 처리
            productInventoryService.restoreQuantity(requestDto);
            acknowledgment.acknowledge(); // 메시지 처리 성공 후 명시적으로 커밋

            String orderCancelMessage = objectMapper.writeValueAsString(key);
            productInventoryService.sendOrderRequest(key.getOrderId().toString(), orderCancelMessage);
            log.info("주문 취소 요청 완료 key: {}, message: {}", key.getOrderId().toString(), orderCancelMessage);
            log.info("재고 복구 처리 완료: {}", message);

        } catch (ApplicationException e) {
            log.error("재고 복구 처리 실패: {}", e.getMessage());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("예기치 않은 오류 발생: {}", e.getMessage());
            acknowledgment.acknowledge(); // 예외 발생 시에도 메시지 중복 처리를 방지
        }
    }

    private void sendRollbackMessage(String topic, String key, String failedMessage) {
        try {
            productInventoryService.sendRollbackRequest(topic, key, failedMessage);
            log.info("롤백 요청 메시지 전송 완료 key: {}, message: {}", key, failedMessage);
        } catch (Exception e) {
            log.error("롤백 요청 메시지 전송 중 오류 발생: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
