package com.sparta.hotdeal.order.application.dtos.basket.res;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostBasketDto {
    private UUID basketId;

    public static ResPostBasketDto createDummyData() {
        return ResPostBasketDto.builder()
                .basketId(UUID.randomUUID())
                .build();
    }
}
