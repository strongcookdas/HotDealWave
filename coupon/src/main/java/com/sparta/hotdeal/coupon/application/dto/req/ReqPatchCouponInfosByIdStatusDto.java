package com.sparta.hotdeal.coupon.application.dto.req;

import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqPatchCouponInfosByIdStatusDto {
    private CouponStatus status; // 쿠폰 상태 (예: PENDING, ISSUED, OUT_OF_STOCK)
}
