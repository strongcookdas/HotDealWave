package com.sparta.hotdeal.product.application.dtos.res.product;

import com.sparta.hotdeal.product.domain.entity.product.PromotionStatusEnum;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResGetPromotionDto {
    private UUID promotionId;
    private UUID productId;
    private Timestamp start;
    private Timestamp end;
    private Integer discountRate;
    private Integer discountPrice;
    private Integer quantity;
    private Integer remaining;
    private PromotionStatusEnum status;
}
