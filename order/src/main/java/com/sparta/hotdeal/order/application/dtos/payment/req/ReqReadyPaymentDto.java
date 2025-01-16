package com.sparta.hotdeal.order.application.dtos.payment.req;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqReadyPaymentDto {
    private UUID orderId;

    public static ReqReadyPaymentDto create(UUID orderId) {
        return ReqReadyPaymentDto.builder()
                .orderId(orderId)
                .build();
    }
}
