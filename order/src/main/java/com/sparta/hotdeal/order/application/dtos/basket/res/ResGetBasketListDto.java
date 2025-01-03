package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResGetBasketListDto {
    private UUID basketId;
    private UUID productId;
    private String productName;
    private Integer productQuantity;
    private Integer productPrice;

    public static ResGetBasketListDto from(Basket basket, ProductListDto product){
        return ResGetBasketListDto.builder()
                .basketId(basket.getId())
                .productId(product.getProductId())
                .productName(product.getName())
                .productQuantity(basket.getQuantity())
                .productPrice(product.getPrice())
                .build();
    }
}
