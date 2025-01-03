package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResDeleteBasketDto {
    private UUID basketId;

    public static ResDeleteBasketDto of(Basket basket) {
        return ResDeleteBasketDto.builder()
                .basketId(basket.getId())
                .build();
    }
}
