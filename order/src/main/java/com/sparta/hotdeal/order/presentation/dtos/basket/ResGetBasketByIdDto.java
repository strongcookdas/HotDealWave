package com.sparta.hotdeal.order.presentation.dtos.basket;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResGetBasketByIdDto {
    private UUID basketId;
    private UUID productId;
    private String productName;
    private Integer productQuantity;
    private Integer productPrice;

    public static ResGetBasketByIdDto createDummyData(UUID basketId) {
        return ResGetBasketByIdDto.builder()
                .basketId(basketId)
                .productId(UUID.randomUUID())
                .productName("product Sample")
                .productQuantity(2)
                .productPrice(5000)
                .build();
    }
}
