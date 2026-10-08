package com.sparta.hotdeal.product.infrastructure.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutProductQuantityDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.product.ProductInventoryService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProductKafkaConsumerTest {

    @Mock
    private ProductInventoryService productInventoryService;

    @Mock
    private Acknowledgment acknowledgment;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductKafkaConsumer productKafkaConsumer;

    @BeforeEach
    void setUp() {
        productKafkaConsumer = new ProductKafkaConsumer(productInventoryService, objectMapper);
        ReflectionTestUtils.setField(productKafkaConsumer, "requestOrderTopic", "cancel-order");
    }

    private String message(UUID orderId) throws Exception {
        ReqPutProductQuantityDto dto = new ReqPutProductQuantityDto(
                orderId, List.of(new ReqPutProductQuantityDto.ProductQuantityDetail(UUID.randomUUID(), 1))
        );
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    @DisplayName("재고 차감과 결제 요청이 모두 성공하면 롤백 메시지는 발행되지 않는다")
    void consumeReduceQuantity_success() throws Exception {
        String message = message(UUID.randomUUID());

        productKafkaConsumer.consumeReduceQuantity(message, acknowledgment);

        verify(productInventoryService).sendPaymentRequest(anyString(), anyString());
        verify(productInventoryService, never()).sendRollbackRequest(anyString(), anyString(), anyString());
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    @DisplayName("재고 부족(ApplicationException) 시 롤백 메시지를 발행한다")
    void consumeReduceQuantity_applicationException_rollsBack() throws Exception {
        String message = message(UUID.randomUUID());
        doThrow(new ApplicationException(ErrorCode.PRODUCT_INVENTORY_UPDATE_FAILED_EXCEPTION))
                .when(productInventoryService).reduceQuantity(any());

        productKafkaConsumer.consumeReduceQuantity(message, acknowledgment);

        verify(productInventoryService).sendRollbackRequest(anyString(), anyString(), anyString());
        verify(productInventoryService, never()).sendPaymentRequest(anyString(), anyString());
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    @DisplayName("재고 차감은 성공했지만 결제 요청 발행이 실패하면 롤백 메시지를 발행해야 한다 - 현재는 실패하는 테스트(버그 재현)")
    void consumeReduceQuantity_paymentRequestPublishFails_mustRollBack() throws Exception {
        String message = message(UUID.randomUUID());
        doThrow(new RuntimeException("kafka broker down"))
                .when(productInventoryService).sendPaymentRequest(anyString(), anyString());

        productKafkaConsumer.consumeReduceQuantity(message, acknowledgment);

        verify(productInventoryService).sendRollbackRequest(anyString(), anyString(), anyString());
        verify(acknowledgment, atLeastOnce()).acknowledge();
    }
}