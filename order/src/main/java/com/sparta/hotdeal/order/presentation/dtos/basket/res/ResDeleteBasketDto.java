package com.sparta.hotdeal.order.presentation.dtos.basket.res;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResDeleteBasketDto {
    private UUID basketId;
    public static ResDeleteBasketDto createDummyData() {
        return ResDeleteBasketDto.builder()
                .basketId(UUID.randomUUID())
                .build();
    }
}
