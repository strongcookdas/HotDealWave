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


    public static List<ResGetBasketListDto> createDummyList() {
        List<ResGetBasketListDto> dummyBaskets = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            ResGetBasketListDto basket = ResGetBasketListDto.builder()
                    .basketId(UUID.randomUUID())
                    .productId(UUID.randomUUID())
                    .productName("Product " + i)
                    .productQuantity(i)
                    .productPrice(10000 * i)
                    .build();
            dummyBaskets.add(basket);
        }

        return dummyBaskets;
    }
}
