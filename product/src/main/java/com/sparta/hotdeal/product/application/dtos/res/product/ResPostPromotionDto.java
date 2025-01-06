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
public class ResPostPromotionDto {
    private UUID promotionId;

    public static ResPostPromotionDto of(UUID promotionId) {
        return ResPostPromotionDto.builder()
                .promotionId(promotionId)
                .build();
    }
}
