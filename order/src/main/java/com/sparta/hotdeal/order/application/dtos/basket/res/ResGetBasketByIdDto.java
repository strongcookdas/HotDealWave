package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductByIdForBasketDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetBasketByIdDto {
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

        public static Product of(ResGetProductByIdForBasketDto resGetProductByIdForBasketDto) {
            return Product.builder()
                    .productId(resGetProductByIdForBasketDto.getProductId())
                    .productName(resGetProductByIdForBasketDto.getName())
                    .productQuantity(resGetProductByIdForBasketDto.getQuantity())
                    .productPrice(resGetProductByIdForBasketDto.getPrice())
                    .productStatus(resGetProductByIdForBasketDto.getStatus())
                    .productDiscountPrice(resGetProductByIdForBasketDto.getDiscountPrice())
                    .productRating(resGetProductByIdForBasketDto.getRating())
                    .productReviewCnt(resGetProductByIdForBasketDto.getReviewCnt())
                    .productThumbImg(resGetProductByIdForBasketDto.getThumbImg())
                    .build();
        }
    }

    public static ResGetBasketByIdDto of(Basket basket, ResGetProductByIdForBasketDto resGetProductByIdForBasketDto) {
        return ResGetBasketByIdDto.builder()
                .basketId(basket.getId())
                .quantity(basket.getQuantity())
                .product(Product.of(resGetProductByIdForBasketDto))
                .build();
    }
}
