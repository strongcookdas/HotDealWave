package com.sparta.hotdeal.coupon.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

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
