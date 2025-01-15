package com.sparta.hotdeal.product.application.dtos.req.product;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPutProductQuantityDto {
    private UUID productId;
    private int quantity;
}
