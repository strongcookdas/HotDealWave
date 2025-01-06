package com.sparta.hotdeal.order.application.dtos.order.res;

import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ProductDto;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetOrderListDto {
    private UUID orderId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<Product> productList;

    @Getter
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private Integer productPrice;

        public static Product of(ProductDto product, OrderProductDto orderProductDto) {
            return Product.builder()
                    .productId(product.getProductId())
                    .productName(product.getName())
                    .productQuantity(orderProductDto.getQuantity())
                    .productPrice(orderProductDto.getPrice())
                    .build();
        }
    }

    public static ResGetOrderListDto of(Order order, List<OrderProductDto> orderProductDtoList,
                                        Map<UUID, ProductDto> productMap) {
        return ResGetOrderListDto.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .productList(orderProductDtoList.stream().map(orderProductDto -> {
                    ProductDto product = productMap.get(orderProductDto.getProductId());
                    return Product.of(product, orderProductDto);
                }).toList())
                .build();
    }
}
