package com.sparta.hotdeal.order.presentation.dtos.basket.req;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReqPostBasketDto {
    private UUID privateId;
    private Integer quantity;
}
