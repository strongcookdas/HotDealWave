package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResDeleteKakaoPayCancelDto {

    private String tid;
    private String cid;
    private String status;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private String itemName;
    private int quantity;
    private Amount amount;
    private Amount approvedCancelAmount;
    private Amount canceledAmount;
    private Amount cancelAvailableAmount;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Amount {
        private int total;
        private int taxFree;
        private int vat;
        private int point;
        private int discount;
        private int greenDeposit;

        public KakaoPayCancelDto.Amount toAmountDto() {
            return KakaoPayCancelDto.Amount.builder()
                    .total(total)
                    .taxFree(taxFree)
                    .vat(vat)
                    .point(point)
                    .discount(discount)
                    .greenDeposit(greenDeposit)
                    .build();
        }
    }

    public KakaoPayCancelDto toKakaoPayCancelDto() {
        return KakaoPayCancelDto.builder()
                .tid(tid)
                .cid(cid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .paymentMethodType(paymentMethodType)
                .itemName(itemName)
                .quantity(quantity)
                .amount(amount.toAmountDto())
                .approvedCancelAmount(approvedCancelAmount.toAmountDto())
                .canceledAmount(canceledAmount.toAmountDto())
                .cancelAvailableAmount(cancelAvailableAmount.toAmountDto())
                .createdAt(createdAt)
                .approvedAt(approvedAt)
                .canceledAt(canceledAt)
                .build();
    }
}
