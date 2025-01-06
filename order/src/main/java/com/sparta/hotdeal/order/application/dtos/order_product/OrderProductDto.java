package com.sparta.hotdeal.order.application.dtos.order_product;

import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderProductDto {
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;
    private Integer price;

    public static OrderProductDto of(OrderProduct orderProduct) {
        return OrderProductDto.builder()
                .id(orderProduct.getId())
                .productId(orderProduct.getProductId())
                .orderId(orderProduct.getOrderId())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getPrice())
                .build();
    }
}
