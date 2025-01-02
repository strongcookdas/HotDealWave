package com.sparta.hotdeal.coupon.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResGetUserCouponsDto {
    private UUID couponId;
    private String name;
    private int discountAmount;
    private int minOrderAmount;
    private LocalDate expirationDate;
    private boolean isUsed;
    private LocalDate usedDate;
    private LocalDate dailyIssuedDate;
}
