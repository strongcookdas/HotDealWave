package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostBasketDto {
    private UUID basketId;

    public static ResPostBasketDto of(Basket basket) {
        return ResPostBasketDto.builder()
                .basketId(basket.getId())
                .build();
    }
}
