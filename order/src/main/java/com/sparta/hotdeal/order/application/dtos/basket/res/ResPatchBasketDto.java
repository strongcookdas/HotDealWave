package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPatchBasketDto {
    private UUID basketId;

    public static ResPatchBasketDto of(Basket basket) {
        return ResPatchBasketDto.builder()
                .basketId(basket.getId())
                .build();
    }
}
