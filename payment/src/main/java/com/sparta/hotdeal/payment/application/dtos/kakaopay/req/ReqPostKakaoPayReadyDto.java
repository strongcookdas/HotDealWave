package com.sparta.hotdeal.payment.application.dtos.kakaopay.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostKakaoPayReadyDto {
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private Integer totalAmount;
    private Integer taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;
}
