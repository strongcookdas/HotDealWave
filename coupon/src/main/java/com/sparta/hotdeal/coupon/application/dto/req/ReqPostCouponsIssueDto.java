package com.sparta.hotdeal.coupon.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqPostCouponsIssueDto {
    private UUID couponInfoId; // 발급할 쿠폰의 정보 ID
}
