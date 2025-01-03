package com.sparta.hotdeal.order.application.dtos.basket;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreateBasketDto { // 중간 DTO 이름 컨벤션을 정하지 않음
    private UUID productId;
    private Integer quantity;

    public static CreateBasketDto create(UUID productId, Integer quantity) {
        return CreateBasketDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
