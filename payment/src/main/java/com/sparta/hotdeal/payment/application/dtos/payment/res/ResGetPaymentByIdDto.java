package com.sparta.hotdeal.payment.application.dtos.payment.res;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResGetPaymentByIdDto {
    private UUID paymentId;
    private UUID orderId;
    private PaymentStatus paymentStatus;
    private Integer paymentAmount;
    private Integer refundAmount;

    public static ResGetPaymentByIdDto of(Payment payment){
        return ResGetPaymentByIdDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentStatus(payment.getStatus())
                .paymentAmount(payment.getAmount())
                .refundAmount(payment.getRefundAmount())
                .build();
    }
}
