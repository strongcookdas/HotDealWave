package com.sparta.hotdeal.payment.application.dtos.payment.req;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqPostPaymentDto {
    private UUID orderId;
    private UUID userId;
    private String orderName;
    private Integer Quantity;
    private Integer totalAmount;
}
