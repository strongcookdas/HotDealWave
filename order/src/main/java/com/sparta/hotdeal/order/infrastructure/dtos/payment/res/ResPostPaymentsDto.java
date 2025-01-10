package com.sparta.hotdeal.order.infrastructure.dtos.payment.res;

import com.sparta.hotdeal.order.application.dtos.payment.PaymentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPostPaymentsDto {
    private String tid;
    private String nextRedirectPcUrl;
    private String createdAt;

    public PaymentRequestDto toPaymentRequestDto() {
        return PaymentRequestDto.builder()
                .tid(tid)
                .nextRedirectPcUrl(nextRedirectPcUrl)
                .createdAt(createdAt)
                .build();
    }
}
