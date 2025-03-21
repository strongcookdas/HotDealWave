package com.sparta.hotdeal.product.application.dtos.res.product;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResPatchProductStatusDto {
    private UUID productId;

    public static ResPatchProductStatusDto of(UUID productId) {
        return ResPatchProductStatusDto.builder()
                .productId(productId)
                .build();
    }
}
