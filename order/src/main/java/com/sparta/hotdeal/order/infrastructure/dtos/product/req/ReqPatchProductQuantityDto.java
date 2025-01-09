package com.sparta.hotdeal.order.infrastructure.dtos.product.req;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqPatchProductQuantityDto {
    private UUID productId;
    private int quantity;

    public static ReqPatchProductQuantityDto create(UUID productId, Integer quantity) {
        return ReqPatchProductQuantityDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
