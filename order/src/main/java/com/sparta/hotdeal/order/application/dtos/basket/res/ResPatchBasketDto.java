package com.sparta.hotdeal.order.application.dtos.basket.res;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPatchBasketDto {
    private UUID basketId;

    public static ResPatchBasketDto createDummyData() {
        return ResPatchBasketDto.builder()
                .basketId(UUID.randomUUID())
                .build();
    }
}
