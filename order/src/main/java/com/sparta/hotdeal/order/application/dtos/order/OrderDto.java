package com.sparta.hotdeal.order.application.dtos.order;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDto {
    private UUID id;
    private UUID addressId;
    private UUID userId;
    private OrderStatus status;
    private Integer totalAmount;
    private String name;
    private UUID couponId;
    private Integer couponDiscountAmount;

    public static OrderDto of(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .addressId(order.getAddressId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .name(order.getName())
                .status(order.getStatus())
                .couponId(order.getCouponId())
                .couponDiscountAmount(order.getCouponDiscountAmount())
                .build();
    }
}
