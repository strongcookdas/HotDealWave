package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReqPostKakaoPayReadyDto {
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private Integer quantity;
    private Integer totalAmount;
    private Integer taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;

    public static ReqPostKakaoPayReadyDto create(
            String cid,
            UUID orderId,
            UUID userId,
            String orderName,
            Integer quantity,
            Integer totalAmount,
            String domain
    ){
        return ReqPostKakaoPayReadyDto.builder()
                .cid(cid)
                .partnerOrderId(orderId.toString())
                .partnerUserId(userId.toString())
                .itemName(orderName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .taxFreeAmount(0)
                .approvalUrl(domain + "/payment/success")
                .cancelUrl(domain + "/payment/fail")
                .failUrl(domain + "/payment/cancel")
                .build();
    }
}
