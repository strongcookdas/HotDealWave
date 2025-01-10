package com.sparta.hotdeal.product.application.dtos.req.promotion;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPromotionQuantityDto {
    private UUID productId;
    private int quantity;
}
