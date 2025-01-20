package com.sparta.hotdeal.order.application.dtos.payment.req;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPaymentCancelMessage {
    private UUID orderId;

    public static ReqPaymentCancelMessage of(Order order) {
        return ReqPaymentCancelMessage.builder()
                .orderId(order.getId())
                .build();
    }
}
