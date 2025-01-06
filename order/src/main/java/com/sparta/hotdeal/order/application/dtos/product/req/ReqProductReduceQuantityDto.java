package com.sparta.hotdeal.order.application.dtos.product.req;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ReqProductReduceQuantityDto {
    private UUID productId;
    private Integer quantity;

    public static ReqProductReduceQuantityDto of(UUID productId, Integer quantity) {
        return ReqProductReduceQuantityDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
