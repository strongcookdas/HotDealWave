package com.sparta.hotdeal.product.application.dtos.req.product;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPostPromotionDto {
    private UUID productId;
    private Timestamp start;
    private Timestamp end;
    private Integer discountPrice;
    private Integer quantity;
}
