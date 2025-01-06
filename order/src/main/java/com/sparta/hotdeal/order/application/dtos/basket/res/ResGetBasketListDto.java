package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForBasketDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetBasketListDto {
    private UUID basketId;
    private Integer quantity;
    private Product product;

    @Getter
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private Integer productPrice;
        private String productStatus;
        private Integer productDiscountPrice;
        private double productRating;
        private Integer productReviewCnt;
        private String productThumbImg;


        public static Product of(ResGetProductListForBasketDto resGetProductListForBasketDto) {
            return Product.builder()
                    .productId(resGetProductListForBasketDto.getProductId())
                    .productName(resGetProductListForBasketDto.getName())
                    .productQuantity(resGetProductListForBasketDto.getQuantity())
                    .productPrice(resGetProductListForBasketDto.getPrice())
                    .productStatus(resGetProductListForBasketDto.getStatus())
                    .productDiscountPrice(resGetProductListForBasketDto.getDiscountPrice())
                    .productRating(resGetProductListForBasketDto.getRating())
                    .productReviewCnt(resGetProductListForBasketDto.getReviewCnt())
                    .productThumbImg(resGetProductListForBasketDto.getThumbImg())
                    .build();
        }
    }

    public static ResGetBasketListDto of(Basket basket, ResGetProductListForBasketDto resGetProductListForBasketDto) {
        return ResGetBasketListDto.builder()
                .basketId(basket.getId())
                .quantity(basket.getQuantity())
                .product(Product.of(resGetProductListForBasketDto))
                .build();
    }
}
