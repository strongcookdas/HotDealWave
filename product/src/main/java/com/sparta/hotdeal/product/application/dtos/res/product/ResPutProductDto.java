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
public class ResPutProductDto {
    private UUID productId;

    public static ResPutProductDto of(UUID productId) {
        return ResPutProductDto.builder()
                .productId(productId)
                .build();
    }
}
