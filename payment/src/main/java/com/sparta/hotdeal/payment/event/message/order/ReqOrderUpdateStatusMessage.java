package com.sparta.hotdeal.payment.event.message.order;

import com.sparta.hotdeal.payment.domain.entity.order.OrderStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqOrderUpdateStatusMessage {
    private UUID orderId;
    private OrderStatus status;

    public static ReqOrderUpdateStatusMessage of(UUID orderId, OrderStatus status) {
        return ReqOrderUpdateStatusMessage.builder()
                .orderId(orderId)
                .status(status)
                .build();
    }
}
