package com.sparta.hotdeal.order.application.dtos.basket.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
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


        public static Product of(ProductListDto productListDto) {
            return Product.builder()
                    .productId(productListDto.getProductId())
                    .productName(productListDto.getName())
                    .productQuantity(productListDto.getQuantity())
                    .productPrice(productListDto.getPrice())
                    .productStatus(productListDto.getStatus())
                    .productDiscountPrice(productListDto.getDiscountPrice())
                    .productRating(productListDto.getRating())
                    .productReviewCnt(productListDto.getReviewCnt())
                    .productThumbImg(productListDto.getThumbImg())
                    .build();
        }
    }

    public static ResGetBasketListDto of(Basket basket, ProductListDto productListDto) {
        return ResGetBasketListDto.builder()
                .basketId(basket.getId())
                .quantity(basket.getQuantity())
                .product(Product.of(productListDto))
                .build();
    }
}
