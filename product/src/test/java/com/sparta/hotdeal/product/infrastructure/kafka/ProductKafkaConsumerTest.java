package com.sparta.hotdeal.product.infrastructure.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
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
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    @DisplayName("재고 차감은 성공했지만 결제 요청 발행이 실패하면 롤백 메시지를 발행해야 한다")
    void consumeReduceQuantity_paymentRequestPublishFails_mustRollBack() throws Exception {
        String message = message(UUID.randomUUID());
        doThrow(new RuntimeException("kafka broker down"))
                .when(productInventoryService).sendPaymentRequest(anyString(), anyString());

        productKafkaConsumer.consumeReduceQuantity(message, acknowledgment);

        verify(productInventoryService).sendRollbackRequest(anyString(), anyString(), anyString());
        verify(acknowledgment, atLeastOnce()).acknowledge();
    }

    @Test
    @DisplayName("결제 요청 발행 실패 시, 트랜잭션이 활성화되어 있다면 재고 차감을 롤백 전용으로 표시해야 한다")
    void consumeReduceQuantity_paymentRequestPublishFails_marksTransactionRollbackOnly() throws Exception {
        String message = message(UUID.randomUUID());
        doThrow(new RuntimeException("kafka broker down"))
                .when(productInventoryService).sendPaymentRequest(anyString(), anyString());

        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        try (MockedStatic<TransactionSynchronizationManager> syncManager =
                     mockStatic(TransactionSynchronizationManager.class);
             MockedStatic<TransactionAspectSupport> aspectSupport = mockStatic(TransactionAspectSupport.class)) {
            syncManager.when(TransactionSynchronizationManager::isActualTransactionActive).thenReturn(true);
            aspectSupport.when(TransactionAspectSupport::currentTransactionStatus).thenReturn(transactionStatus);

            productKafkaConsumer.consumeReduceQuantity(message, acknowledgment);

            verify(transactionStatus).setRollbackOnly();
        }
        verify(productInventoryService).sendRollbackRequest(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("결제 요청 발행 실패 시, 활성화된 트랜잭션이 없으면 롤백 표시를 시도하지 않고도 롤백 메시지는 보낸다")
    void consumeReduceQuantity_paymentRequestPublishFails_noActiveTransaction_doesNotThrow() throws Exception {
        String message = message(UUID.randomUUID());
        doThrow(new RuntimeException("kafka broker down"))
                .when(productInventoryService).sendPaymentRequest(anyString(), anyString());

        productKafkaConsumer.consumeReduceQuantity(message, acknowledgment);

        verify(productInventoryService).sendRollbackRequest(anyString(), anyString(), anyString());
    }
}