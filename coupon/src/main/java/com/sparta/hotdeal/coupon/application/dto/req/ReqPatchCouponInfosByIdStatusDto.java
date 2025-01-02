package com.sparta.hotdeal.coupon.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqPatchCouponInfosByIdStatusDto {
    private String status; // 쿠폰 상태 (예: PENDING, ISSUED, OUT_OF_STOCK)
}
