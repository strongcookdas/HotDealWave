package com.sparta.hotdeal.payment.application.dtos.payment;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentDto {
    private UUID paymentId;
    private UUID orderId;
    private UUID userId;
    private PaymentStatus status;
    private Integer amount;
    private Integer refundAmount;
    private String tid;

    public static PaymentDto from(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .status(payment.getStatus())
                .refundAmount(payment.getRefundAmount())
                .tid(payment.getTid())
                .build();
    }
}
