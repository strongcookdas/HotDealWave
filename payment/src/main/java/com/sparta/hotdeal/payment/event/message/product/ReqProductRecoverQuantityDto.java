package com.sparta.hotdeal.payment.event.message.product;

import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
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
public class ReqProductRecoverQuantityDto {
    private UUID orderId;
    private List<Product> productList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Product {
        private UUID productId;
        private int quantity;

        public static Product of(OrderDto.Product product) {
            return Product.builder()
                    .productId(product.getProductId())
                    .quantity(product.getProductQuantity())
                    .build();
        }
    }

    public static ReqProductRecoverQuantityDto of(OrderDto orderDto){
        return ReqProductRecoverQuantityDto.builder()
                .orderId(orderDto.getOrderId())
                .productList(orderDto.getProductList().stream().map(Product::of).toList())
                .build();
    }
}
