package com.sparta.hotdeal.product.application.dtos.res.promotion;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResPutPromotionDto {
    private UUID promotionId;

    public static ResPutPromotionDto of(UUID promotionId) {
        return ResPutPromotionDto.builder()
                .promotionId(promotionId)
                .build();
    }
}
