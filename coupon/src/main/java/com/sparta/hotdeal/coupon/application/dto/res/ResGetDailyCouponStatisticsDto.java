package com.sparta.hotdeal.coupon.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResGetDailyCouponStatisticsDto {
    private int totalUsers;       // 총 사용자 수
    private int averageDiscount;  // 평균 할인 금액
    private int totalDiscount;    // 총 할인 금액
}
