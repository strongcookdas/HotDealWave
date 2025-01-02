package com.sparta.hotdeal.order.presentation.dtos.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class ResGetOrdersDto {
    private UUID orderId;
    private LocalDate createdAt;
    private List<Product> productList;

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

    // 더미 데이터 3개 생성 메서드
    public static List<ResGetOrdersDto> createDummyList() {
        List<ResGetOrdersDto> dummyOrders = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            List<Product> products = List.of(
                    Product.builder()
                            .productId(UUID.randomUUID())
                            .productName("Product A" + i)
                            .productQuantity(1)
                            .productPrice(20000 + i * 1000)
                            .orderStatus("COMPLETE")
                            .deliveryStatus("COMPLETE")
                            .deliveryDate(LocalDate.now().plusDays(i))
                            .build(),
                    Product.builder()
                            .productId(UUID.randomUUID())
                            .productName("Product B" + i)
                            .productQuantity(2)
                            .productPrice(30000 + i * 1000)
                            .orderStatus("COMPLETE")
                            .deliveryStatus("COMPLETE")
                            .deliveryDate(LocalDate.now().plusDays(i + 1))
                            .build()
            );

            ResGetOrdersDto order = ResGetOrdersDto.builder()
                    .orderId(UUID.randomUUID())
                    .createdAt(LocalDate.now().minusDays(i))
                    .productList(products)
                    .build();

            dummyOrders.add(order);
        }

        return dummyOrders;
    }
}
