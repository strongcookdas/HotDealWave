package com.sparta.hotdeal.order.application.dtos.coupon;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CouponDto {
    private UUID couponId;
    private Integer discountAmount;
    private Integer minOrderAmount;
    private LocalDate expirationDate;
    private UUID companyId;
    private Boolean isUsed;

    public static CouponDto of() {
        return CouponDto.builder().build();
    }

    public static CouponDto createDummy() {
        return CouponDto.builder()
                .couponId(UUID.fromString("147b6a8d-c7d4-4d6f-875c-48a651670c9d"))
                .discountAmount(2000)
                .minOrderAmount(20000)
                .expirationDate(LocalDate.of(2025, 1, 31))
                .companyId(UUID.fromString("ac48a561-8994-43e9-a0b1-9555f4fc2fbe"))
                .isUsed(false)
                .build();
    }
}
