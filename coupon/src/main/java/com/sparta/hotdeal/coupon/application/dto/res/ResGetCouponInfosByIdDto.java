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
public class ResGetCouponInfosByIdDto {
    private UUID couponId;
    private String name;
    private int quantity;
    private int issuedCount;
    private int discountAmount;
    private int minOrderAmount;
    private LocalDate expirationDate;
    private String status; // 예: PENDING, ISSUED, OUT_OF_STOCK
    private String couponType; // 예: FIRST_COME_FIRST_SERVE, DAILY_COUPON
    private UUID companyId;
}
