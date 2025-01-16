package com.sparta.hotdeal.order.event.message;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqOrderCancelMessage {
    private UUID orderId;
}
