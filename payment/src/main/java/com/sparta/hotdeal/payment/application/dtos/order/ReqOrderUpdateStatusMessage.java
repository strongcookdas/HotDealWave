package com.sparta.hotdeal.payment.application.dtos.order;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqOrderUpdateStatusMessage {
    private UUID orderId;
    private String status;

    public static ReqOrderUpdateStatusMessage of(UUID orderId, String status) {
        return ReqOrderUpdateStatusMessage.builder()
                .orderId(orderId)
                .status(status)
                .build();
    }
}
