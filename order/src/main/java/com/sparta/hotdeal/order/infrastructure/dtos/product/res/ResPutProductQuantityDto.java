package com.sparta.hotdeal.order.infrastructure.dtos.product.res;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResPutProductQuantityDto {
    private UUID orderId;

    public static ResPutProductQuantityDto of(UUID orderId) {
        return ResPutProductQuantityDto.builder()
                .orderId(orderId)
                .build();
    }
}
