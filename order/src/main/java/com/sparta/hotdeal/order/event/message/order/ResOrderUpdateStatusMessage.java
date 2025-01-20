package com.sparta.hotdeal.order.event.message.order;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResOrderUpdateStatusMessage {
    private UUID orderId;
    private String status;
}
