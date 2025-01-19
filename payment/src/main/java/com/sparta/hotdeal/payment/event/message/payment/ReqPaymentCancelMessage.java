package com.sparta.hotdeal.payment.event.message.payment;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPaymentCancelMessage {
    private UUID orderId;
}
