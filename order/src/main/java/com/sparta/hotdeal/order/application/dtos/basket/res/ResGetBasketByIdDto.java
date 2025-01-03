package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
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
    private Integer quantity;
    private UUID productId;
    private String productName;
    private Integer productPrice;

    public static ResGetBasketByIdDto of(Basket basket, ProductDto product) {
        return ResGetBasketByIdDto.builder()
                .basketId(basket.getId())
                .quantity(basket.getQuantity())
                .productId(product.getProductId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .build();
    }
}
