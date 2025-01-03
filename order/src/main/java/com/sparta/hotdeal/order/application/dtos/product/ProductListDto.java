package com.sparta.hotdeal.order.application.dtos.product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ProductListDto {
    private UUID productId;
    private String name;
    private Integer price;
    private String category;
    private String thumbImg;
    private Integer discountPrice;

    public static ProductListDto create(
            UUID productId,
            String name,
            Integer price,
            String category,
            String thumbImg,
            Integer discountPrice
    ) {
        return ProductListDto.builder()
                .productId(productId)
                .name(name)
                .price(price)
                .category(category)
                .thumbImg(thumbImg)
                .discountPrice(discountPrice)
                .build();
    }
}
