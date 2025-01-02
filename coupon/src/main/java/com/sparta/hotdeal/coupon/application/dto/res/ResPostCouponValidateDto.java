package com.sparta.hotdeal.coupon.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResPostCouponValidateDto {
    private boolean isValid; // 쿠폰 사용 가능 여부
}
