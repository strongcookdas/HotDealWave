package com.sparta.hotdeal.order.presentation.dtos.basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqPatchBasketDto {
    private Integer quantity;
}
