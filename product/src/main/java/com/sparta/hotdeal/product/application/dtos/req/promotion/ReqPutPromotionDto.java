package com.sparta.hotdeal.product.application.dtos.req.promotion;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPutPromotionDto {
    private UUID productId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer discountPrice;
    private Integer quantity;
}
