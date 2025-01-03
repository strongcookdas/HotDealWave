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
public class ResPostProductDto {
    private UUID productId;

    public static ResPostProductDto of(UUID productId) {
        return ResPostProductDto.builder()
                .productId(productId)
                .build();
    }
}
