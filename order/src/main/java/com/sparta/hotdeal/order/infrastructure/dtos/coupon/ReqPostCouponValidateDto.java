package com.sparta.hotdeal.order.infrastructure.dtos.coupon;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqPostCouponValidateDto {
    private List<Product> products;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private UUID companyId;
        private int price;
    }
}

