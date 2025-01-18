package com.sparta.hotdeal.order.application.dtos.payment.req;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPaymentRefundMessage {
    private UUID orderId;

    public static ReqPaymentRefundMessage of(Order order) {
        return ReqPaymentRefundMessage.builder()
                .orderId(order.getId())
                .build();
    }
}
