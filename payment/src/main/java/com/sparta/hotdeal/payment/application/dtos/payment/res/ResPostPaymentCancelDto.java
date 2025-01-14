package com.sparta.hotdeal.payment.application.dtos.payment.res;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostPaymentCancelDto {
    private String tid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private int quantity;
    private Integer refundAmount;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public static ResPostPaymentCancelDto from(KakaoPayCancelDto kakaoPayCancelDto) {
        return ResPostPaymentCancelDto.builder()
                .tid(kakaoPayCancelDto.getTid())
                .partnerOrderId(kakaoPayCancelDto.getPartnerOrderId())
                .partnerUserId(kakaoPayCancelDto.getPartnerUserId())
                .itemName(kakaoPayCancelDto.getItemName())
                .quantity(kakaoPayCancelDto.getQuantity())
                .refundAmount(kakaoPayCancelDto.getApprovedCancelAmount().getTotal())
                .createdAt(kakaoPayCancelDto.getCreatedAt())
                .approvedAt(kakaoPayCancelDto.getApprovedAt())
                .canceledAt(kakaoPayCancelDto.getCanceledAt())
                .build();
    }
}
