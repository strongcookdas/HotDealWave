package com.sparta.hotdeal.order.application.dtos.basket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateBasketDto {
    private Integer quantity;

    public static UpdateBasketDto create(Integer quantity) {
        return UpdateBasketDto.builder()
                .quantity(quantity)
                .build();
    }
}
