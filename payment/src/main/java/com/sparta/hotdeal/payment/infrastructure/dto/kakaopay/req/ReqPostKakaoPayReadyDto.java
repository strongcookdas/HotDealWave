package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
            UUID orderId,
            UUID userId,
            String orderName,
            Integer quantity,
            Integer totalAmount
    ){
        return ReqPostKakaoPayReadyDto.builder()
                .cid("cid")
                .partnerOrderId(orderId.toString())
                .partnerUserId(userId.toString())
                .itemName(orderName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .taxFreeAmount(0)
                .approvalUrl("url")
                .cancelUrl("url")
                .failUrl("url")
                .build();
    }
}
