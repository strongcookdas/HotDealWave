package com.sparta.hotdeal.order.application.dtos.product.req;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqPatchProductQuantityDto {
    private UUID orderId;
    private List<Product> productList;


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Product {
        private UUID productId;
        private int quantity;

        public static Product of(Basket basket) {
            return Product.builder()
                    .productId(basket.getProductId())
                    .quantity(basket.getQuantity())
                    .build();
        }
    }

    public static ReqPatchProductQuantityDto create(UUID orderId, List<Basket> basketList) {
        return ReqPatchProductQuantityDto.builder()
                .orderId(orderId)
                .productList(basketList.stream().map(Product::of).toList())
                .build();
    }
}
