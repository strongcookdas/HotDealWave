package com.sparta.hotdeal.payment.application.dtos.payment.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqPostPaymentConfirmDto {
    private String pgToken;
    private String tid;
}
