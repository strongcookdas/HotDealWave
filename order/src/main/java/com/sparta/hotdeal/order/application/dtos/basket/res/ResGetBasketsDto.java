package com.sparta.hotdeal.order.application.dtos.basket.res;

import java.util.ArrayList;
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
public class ResGetBasketsDto {
    private UUID basketId;
    private UUID productId;
    private String productName;
    private Integer productQuantity;
    private Integer productPrice;
    public static List<ResGetBasketsDto> createDummyList() {
        List<ResGetBasketsDto> dummyBaskets = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            ResGetBasketsDto basket = ResGetBasketsDto.builder()
                    .basketId(UUID.randomUUID())
                    .productId(UUID.randomUUID())
                    .productName("Product " + i)
                    .productQuantity(i)
                    .productPrice(10000 * i)
                    .build();
            dummyBaskets.add(basket);
        }

        return dummyBaskets;
    }
}
