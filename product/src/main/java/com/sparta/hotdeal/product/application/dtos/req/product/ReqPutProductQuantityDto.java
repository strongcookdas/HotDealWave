package com.sparta.hotdeal.product.application.dtos.req.product;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPutProductQuantityDto {
    private UUID orderId;
    private List<ProductQuantityDetail> productList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ProductQuantityDetail {
        private UUID productId;
        private int quantity;
    }
}
