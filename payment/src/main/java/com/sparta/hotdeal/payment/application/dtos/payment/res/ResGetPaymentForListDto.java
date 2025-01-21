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
public class ResGetPaymentForListDto {
    private UUID paymentId;
    private UUID orderId;
    private UUID userId;
    private PaymentStatus paymentStatus;
    private Integer payedAmount;
    private Integer refundAmount;
    private String payUrl;
    private String tid;

    public static ResGetPaymentForListDto of(Payment payment) {
        return ResGetPaymentForListDto.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .paymentStatus(payment.getStatus())
                .payedAmount(payment.getAmount())
                .refundAmount(payment.getRefundAmount())
                .payUrl(payment.getPayUrl())
                .tid(payment.getTid())
                .build();
    }
}