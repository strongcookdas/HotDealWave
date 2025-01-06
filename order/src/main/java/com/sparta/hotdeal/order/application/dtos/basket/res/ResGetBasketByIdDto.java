package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
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

        public static Product of(ProductDto productDto) {
            return Product.builder()
                    .productId(productDto.getProductId())
                    .productName(productDto.getName())
                    .productQuantity(productDto.getQuantity())
                    .productPrice(productDto.getPrice())
                    .productStatus(productDto.getStatus())
                    .productDiscountPrice(productDto.getDiscountPrice())
                    .productRating(productDto.getRating())
                    .productReviewCnt(productDto.getReviewCnt())
                    .productThumbImg(productDto.getThumbImg())
                    .build();
        }
    }

    public static ResGetBasketByIdDto of(Basket basket, ProductDto productDto) {
        return ResGetBasketByIdDto.builder()
                .basketId(basket.getId())
                .quantity(basket.getQuantity())
                .product(Product.of(productDto))
                .build();
    }
}
