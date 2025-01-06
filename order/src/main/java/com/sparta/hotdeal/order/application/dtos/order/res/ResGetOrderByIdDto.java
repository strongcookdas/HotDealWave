package com.sparta.hotdeal.order.application.dtos.order.res;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetOrderByIdDto {
    private UUID orderId;
    private LocalDate createdAt;
    private List<Product> productList;
    private String username;
    private String userPhone;
    private Address address;
    private Integer totalPrice;

    @Getter
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private Integer productPrice;
        private String orderStatus;
        private String deliveryStatus;
        private LocalDate deliveryDate;
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

    // 더미 데이터 생성 메서드
    public static ResGetOrderByIdDto createDummy(UUID orderId) {
        return ResGetOrderByIdDto.builder()
                .orderId(orderId)
                .createdAt(LocalDate.of(2025, 1, 1))
                .username("john_doe")
                .userPhone("010-1234-5678")
                .address(Address.builder()
                        .addressId(UUID.randomUUID())
                        .zipNum("12345")
                        .city("Seoul")
                        .district("Gangnam")
                        .streetName("Teheran-ro")
                        .streetNum("123")
                        .detailAddr("Apartment 101")
                        .build())
                .productList(Arrays.asList(
                        Product.builder()
                                .productId(UUID.randomUUID())
                                .productName("Product A")
                                .productQuantity(1)
                                .productPrice(20000)
                                .orderStatus("COMPLETE")
                                .deliveryStatus("COMPLETE")
                                .deliveryDate(LocalDate.of(2025, 1, 2))
                                .build(),
                        Product.builder()
                                .productId(UUID.randomUUID())
                                .productName("Product B")
                                .productQuantity(2)
                                .productPrice(30000)
                                .orderStatus("COMPLETE")
                                .deliveryStatus("COMPLETE")
                                .deliveryDate(LocalDate.of(2025, 1, 3))
                                .build()
                ))
                .totalPrice(80000)
                .build();
    }
}
