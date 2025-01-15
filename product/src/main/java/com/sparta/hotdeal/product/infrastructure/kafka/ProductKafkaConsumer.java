package com.sparta.hotdeal.product.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductQuantityDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.product.ProductInventoryService;
import java.util.List;
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

    @Value("${spring.kafka.topics.rollback-reduce-quantity}")
    private String rollbackReduceQuantityTopic;

    @KafkaListener(topics = "${spring.kafka.topics.reduce-quantity}", groupId = "product-group")
    @Transactional
    public void consumeReduceQuantity(String message, Acknowledgment acknowledgment) {
        try {
            List<ReqPutProductQuantityDto> requestDtos =
                    objectMapper.readValue(message, objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, ReqPutProductQuantityDto.class));

            productInventoryService.reduceQuantity(requestDtos);
            acknowledgment.acknowledge(); // 메시지 처리 성공 후 명시적으로 커밋
            log.info("재고 차감 처리 완료: {}", message);

        } catch (Exception e) {
            log.error("재고 차감 처리 중 오류 발생: {}", e.getMessage());
            // 재고 차감 중 오류 발생 시 롤백 요청
            sendRollbackMessage(rollbackReduceQuantityTopic, "재고 차감 처리 중 오류 발생");
        }
    }

    private void sendRollbackMessage(String topic, String failedMessage) {
        try {
            productInventoryService.sendRollbackRequest(topic, failedMessage);
        } catch (Exception e) {
            log.error("롤백 요청 메시지 전송 중 오류 발생: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
