package com.sparta.hotdeal.payment.application.dtos.payment.res;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostPaymentCancelDto {
    private UUID orderId;

    public static ResPostPaymentCancelDto from(Payment payment) {
        return ResPostPaymentCancelDto.builder()
                .orderId(payment.getOrderId())
                .build();
    }
}
