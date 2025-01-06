package com.sparta.hotdeal.product.application.dtos.req.product;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPostPromotionDto {
    private UUID productId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer discountPrice;
    private Integer quantity;
}
