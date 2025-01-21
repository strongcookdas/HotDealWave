package com.sparta.hotdeal.payment.application.dtos.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDto {
    private UUID orderId;
    private LocalDateTime createdAt;
    private List<Product> productList;
    private UUID userId;
    private String username;
    private String orderName;
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
    }
}
