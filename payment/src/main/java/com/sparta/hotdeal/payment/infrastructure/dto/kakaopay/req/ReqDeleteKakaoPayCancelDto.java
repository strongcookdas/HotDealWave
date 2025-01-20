package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReqDeleteKakaoPayCancelDto {
    private String cid;
    private String tid;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
    private int cancelAvailableAmount;

    public static ReqDeleteKakaoPayCancelDto create(
            String cid,
            String tid,
            int cancelAmount,
            int cancelTaxFreeAmount,
            int cancelVatAmount,
            int cancelAvailableAmount
    ) {
        return ReqDeleteKakaoPayCancelDto.builder()
                .cid(cid)
                .tid(tid)
                .cancelAmount(cancelAmount)
                .cancelTaxFreeAmount(cancelTaxFreeAmount)
                .cancelVatAmount(cancelVatAmount)
                .cancelAvailableAmount(cancelAvailableAmount)
                .build();
    }
}
