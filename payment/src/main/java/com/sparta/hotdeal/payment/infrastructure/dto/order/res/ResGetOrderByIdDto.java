package com.sparta.hotdeal.payment.infrastructure.dto.order.res;


import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetOrderByIdDto {
    private UUID orderId;
    private LocalDateTime createdAt;
    private List<Product> productList;
    private String orderName;
    private UUID userId;
    private String username;
    private Address address;
    private Integer totalAmount;
    private Integer discountAmount;
    private String orderStatus;

    @Getter
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private Integer productPrice;

        public OrderDto.Product toOrderDtoProduct() {
            return OrderDto.Product.builder()
                    .productId(productId)
                    .productName(productName)
                    .productQuantity(productQuantity)
                    .productPrice(productPrice)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Address {
        private UUID addressId;
        private String zipNum;
        private String city;
        private String district;
        private String streetName;
        private String streetNum;
        private String detailAddr;
    }

    public OrderDto toOrderDto() {
        return OrderDto.builder()
                .orderId(orderId)
                .orderName(orderName)
                .createdAt(createdAt)
                .productList(productList.stream().map(Product::toOrderDtoProduct).toList())
                .userId(userId)
                .username(username)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .orderStatus(orderStatus)
                .build();
    }
}
