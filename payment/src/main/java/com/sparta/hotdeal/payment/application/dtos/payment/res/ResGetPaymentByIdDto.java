package com.sparta.hotdeal.payment.application.dtos.payment.res;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import java.time.LocalDateTime;
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
    private UUID userId;
    private PaymentStatus paymentStatus;
    private Integer payedAmount;
    private Integer refundAmount;
    private LocalDateTime refundedAt;
    private String tid;
    private String payUrl;

    public static ResGetPaymentByIdDto of(Payment payment) {
        return ResGetPaymentByIdDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .paymentStatus(payment.getStatus())
                .payedAmount(payment.getAmount())
                .refundAmount(payment.getRefundAmount())
                .refundedAt(payment.getRefundedAt())
                .tid(payment.getTid())
                .payUrl(payment.getPayUrl())
                .build();
    }
}
