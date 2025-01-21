package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductByIdtDto;
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

        public static Product of(ProductByIdtDto productByIdtDto) {
            return Product.builder()
                    .productId(productByIdtDto.getProductId())
                    .productName(productByIdtDto.getName())
                    .productQuantity(productByIdtDto.getQuantity())
                    .productPrice(productByIdtDto.getPrice())
                    .productStatus(productByIdtDto.getStatus())
                    .productDiscountPrice(productByIdtDto.getDiscountPrice())
                    .productRating(productByIdtDto.getRating())
                    .productReviewCnt(productByIdtDto.getReviewCnt())
                    .productThumbImg(productByIdtDto.getThumbImg())
                    .build();
        }
    }

    public static ResGetBasketByIdDto of(Basket basket, ProductByIdtDto productByIdtDto) {
        return ResGetBasketByIdDto.builder()
                .basketId(basket.getId())
                .quantity(basket.getQuantity())
                .product(Product.of(productByIdtDto))
                .build();
    }
}
