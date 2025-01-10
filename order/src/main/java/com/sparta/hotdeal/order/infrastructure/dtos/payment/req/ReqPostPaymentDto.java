package com.sparta.hotdeal.order.infrastructure.dtos.payment.req;

import com.sparta.hotdeal.order.application.dtos.order.OrderDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReqPostPaymentDto {
    private UUID orderId;
    private String orderName;
    private Integer quantity;
    private Integer totalAmount;

    public static ReqPostPaymentDto create(
            UUID orderId,
            String orderName,
            Integer quantity,
            Integer totalAmount
    ) {
        return ReqPostPaymentDto.builder()
                .orderId(orderId)
                .orderName(orderName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .build();
    }
}
