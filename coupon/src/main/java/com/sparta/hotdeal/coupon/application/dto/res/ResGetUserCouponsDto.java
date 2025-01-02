package com.sparta.hotdeal.coupon.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResGetUserCouponsDto {
    private List<CouponInfo> coupons;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CouponInfo {
        private UUID couponId;
        private String name;
        private int discountAmount;
        private int minOrderAmount;
        private LocalDate expirationDate;
        private boolean isUsed;
        private LocalDate usedDate;
        private LocalDate dailyIssuedDate;
    }
}
