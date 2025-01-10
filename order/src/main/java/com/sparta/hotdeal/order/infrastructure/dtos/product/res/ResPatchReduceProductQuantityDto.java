package com.sparta.hotdeal.order.infrastructure.dtos.product.res;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResPatchReduceProductQuantityDto {
    private UUID productId;

    public static ResPatchReduceProductQuantityDto of(UUID productId) {
        return ResPatchReduceProductQuantityDto.builder()
                .productId(productId)
                .build();
    }
}
