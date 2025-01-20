package com.sparta.hotdeal.order.event.message;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqOrderUpdateStatusMessage {
    private UUID orderId;
    private String status;
}
