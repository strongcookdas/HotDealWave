package com.sparta.hotdeal.order.application.dtos.order.res;

import com.sparta.hotdeal.order.application.dtos.payment.PaymentRequestDto;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostOrderDto {
    private UUID orderId;
    private String tid;
    private String nextRedirectPcUrl;
    private String createdAt;

    public static ResPostOrderDto of(Order order, PaymentRequestDto paymentRequestDto) {
        return ResPostOrderDto.builder()
                .orderId(order.getId())
                .tid(paymentRequestDto.getTid())
                .nextRedirectPcUrl(paymentRequestDto.getNextRedirectPcUrl())
                .createdAt(paymentRequestDto.getCreatedAt())
                .build();
    }
}
