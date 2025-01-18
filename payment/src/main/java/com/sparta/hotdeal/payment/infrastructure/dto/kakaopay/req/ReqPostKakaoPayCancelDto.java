package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReqPostKakaoPayCancelDto {
    private String cid;
    private String tid;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
    private int cancelAvailableAmount;

    public static ReqPostKakaoPayCancelDto create(
            String cid,
            String tid,
            int cancelAmount,
            int cancelTaxFreeAmount,
            int cancelVatAmount,
            int cancelAvailableAmount
    ) {
        return ReqPostKakaoPayCancelDto.builder()
                .cid(cid)
                .tid(tid)
                .cancelAmount(cancelAmount)
                .cancelTaxFreeAmount(cancelTaxFreeAmount)
                .cancelVatAmount(cancelVatAmount)
                .cancelAvailableAmount(cancelAvailableAmount)
                .build();
    }
}
