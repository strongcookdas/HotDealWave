package com.sparta.hotdeal.order.infrastructure.dtos.coupon.res;

import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResPostCouponValidateDto {
    private boolean isValid;
    private int totalDiscountAmount;

    public CouponValidationDto toCouponValidationDto() {
        return CouponValidationDto.create(isValid, totalDiscountAmount);
    }
}
