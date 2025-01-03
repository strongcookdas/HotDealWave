package com.sparta.hotdeal.payment.presentation.dtos.payment.req;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqPostPaymentDto {
    private UUID orderId;
}
