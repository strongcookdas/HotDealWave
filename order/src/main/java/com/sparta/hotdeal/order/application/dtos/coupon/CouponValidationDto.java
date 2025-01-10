package com.sparta.hotdeal.order.application.dtos.coupon;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponValidationDto {
    private boolean isValid;
    private int totalDiscountAmount;

    public static CouponValidationDto create(
            boolean isValid,
            int totalDiscountAmount
    ) {
        return CouponValidationDto.builder()
                .isValid(isValid)
                .totalDiscountAmount(totalDiscountAmount)
                .build();
    }
}
