package com.sparta.hotdeal.product.presentation.dtos.product;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPostCreatePromotionDto {
    private UUID productId;
    private Timestamp start;
    private Timestamp end;
    private Integer discountRate;
    private Integer discountPrice;
    private Integer quantity;
}
