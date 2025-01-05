package com.sparta.hotdeal.order.application.dtos.product.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ResGetProductListForOrderDto {
    private UUID productId;
    private String name;
    private Integer price;
    private String category;
    private String thumbImg;
    private Integer discountPrice;
    private String status;
    private double rating;
    private Integer reviewCnt;
    private Integer quantity;
    private UUID companyId;

    public static ResGetProductListForOrderDto create(
            UUID productId,
            String name,
            Integer price,
            String category,
            String thumbImg,
            Integer discountPrice,
            String status,
            double rating,
            Integer reviewCnt,
            Integer quantity,
            UUID companyId
    ) {
        return ResGetProductListForOrderDto.builder()
                .productId(productId)
                .name(name)
                .price(price)
                .category(category)
                .thumbImg(thumbImg)
                .discountPrice(discountPrice)
                .status(status)
                .rating(rating)
                .reviewCnt(reviewCnt)
                .quantity(quantity)
                .companyId(companyId)
                .build();
    }
}
