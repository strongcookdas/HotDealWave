package com.sparta.hotdeal.order.application.dtos.order.res;

import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ProductDto;
import com.sparta.hotdeal.order.application.dtos.user.ResGetUserByIdForOrderDto;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetOrderByIdDto {
    private UUID orderId;
    private LocalDateTime createdAt;
    private List<Product> productList;
    private String username;
    private String userPhone;
    private Address address;
    private Long totalAmount;
    private Integer discountAmount;
    private OrderStatus orderStatus;

    @Getter
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private Integer productPrice;

        public static Product of(ProductDto product, OrderProductDto orderProduct) {
            return Product.builder()
                    .productId(product.getProductId())
                    .productName(product.getName())
                    .productQuantity(product.getQuantity())
                    .productPrice(orderProduct.getPrice())
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

        public static Address of(ResGetAddressByIdForOrderDto address) {
            return Address.builder()
                    .addressId(address.getAddressId())
                    .zipNum(address.getZipNum())
                    .city(address.getCity())
                    .district(address.getDistrict())
                    .streetName(address.getStreetName())
                    .streetNum(address.getStreetNum())
                    .detailAddr(address.getDetailAddr())
                    .build();
        }
    }

    // 더미 데이터 생성 메서드
    public static ResGetOrderByIdDto createDummy(UUID orderId) {
        return ResGetOrderByIdDto.builder()
                .orderId(orderId)
                .createdAt(LocalDateTime.now())
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
                                .build(),
                        Product.builder()
                                .productId(UUID.randomUUID())
                                .productName("Product B")
                                .productQuantity(2)
                                .productPrice(30000)
                                .build()
                ))
                .totalAmount(80000L)
                .build();
    }

    public static ResGetOrderByIdDto of(Order order, ResGetAddressByIdForOrderDto address,
                                        List<OrderProductDto> orderProductDtoList,
                                        Map<UUID, ProductDto> productMap,
                                        ResGetUserByIdForOrderDto user
    ) {
        return ResGetOrderByIdDto.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .username(user.getNickname())
                .orderStatus(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getCouponDiscountAmount())
                .address(Address.of(address))
                .productList(orderProductDtoList.stream()
                        .map(orderProductDto -> Product.of(
                                productMap.get(orderProductDto.getProductId()),
                                orderProductDto)
                        ).toList())
                .build();
    }
}
