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
public class ResPatchRestoreProductQuantityDto {
    private UUID productId;

    public static ResPatchRestoreProductQuantityDto of(UUID productId) {
        return ResPatchRestoreProductQuantityDto.builder()
                .productId(productId)
                .build();
    }
}
