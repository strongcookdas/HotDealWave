package com.sparta.hotdeal.order.application.dtos.order.res;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostOrderDto {
    private UUID orderId;

    public static ResPostOrderDto of(UUID orderId) {
        return ResPostOrderDto.builder()
                .orderId(orderId)
                .build();
    }
}
