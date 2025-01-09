package com.sparta.hotdeal.order.application.dtos.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDto {
    private String tid;
    private String nextRedirectPcUrl;
    private String createdAt;

    public static PaymentRequestDto create(
            String tid,
            String nextRedirectPcUrl,
            String createdAt
    ) {
        return PaymentRequestDto.builder()
                .tid(tid)
                .nextRedirectPcUrl(nextRedirectPcUrl)
                .createdAt(createdAt)
                .build();
    }
}
