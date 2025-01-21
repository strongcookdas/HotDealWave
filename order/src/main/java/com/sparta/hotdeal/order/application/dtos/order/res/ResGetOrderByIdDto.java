package com.sparta.hotdeal.order.application.dtos.order.res;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.user.UserDto;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import java.time.LocalDateTime;
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
    private UUID userId;
    private String username;
    private String orderName;
    //    private Address address;
    private UUID addressId;
    private Integer totalAmount;
    private Integer discountAmount;
    private OrderStatus orderStatus;

    @Getter
    @Builder
    public static class Product {
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private Integer productPrice;

        public static Product of(ProductDto product, OrderProduct orderProduct) {
            return Product.builder()
                    .productId(product.getProductId())
                    .productName(product.getName())
                    .productQuantity(product.getQuantity())
                    .productPrice(orderProduct.getPrice())
                    .build();
        }
    }

//    @Getter
//    @Builder
//    public static class Address {
//        private UUID addressId;
//        private String zipNum;
//        private String city;
//        private String district;
//        private String streetName;
//        private String streetNum;
//        private String detailAddr;
//
//        public static Address of(AddressDto address) {
//            return Address.builder()
//                    .addressId(address.getAddressId())
//                    .zipNum(address.getZipNum())
//                    .city(address.getCity())
//                    .district(address.getDistrict())
//                    .streetName(address.getStreetName())
//                    .streetNum(address.getStreetNum())
//                    .detailAddr(address.getDetailAddr())
//                    .build();
//        }
//    }

    public static ResGetOrderByIdDto of(Order order,
                                        List<OrderProduct> orderProductDtoList,
                                        Map<UUID, ProductDto> productMap,
                                        UserDto userDto
    ) {
        return ResGetOrderByIdDto.builder()
                .orderId(order.getId())
                .orderName(order.getName())
                .createdAt(order.getCreatedAt())
                .userId(order.getUserId())
                .username(userDto.getNickname())
                .orderStatus(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getCouponDiscountAmount())
//                .address(Address.of(address))
                .addressId(order.getAddressId())
                .productList(orderProductDtoList.stream()
                        .map(orderProductDto -> Product.of(
                                productMap.get(orderProductDto.getProductId()),
                                orderProductDto)
                        ).toList())
                .build();
    }
}
