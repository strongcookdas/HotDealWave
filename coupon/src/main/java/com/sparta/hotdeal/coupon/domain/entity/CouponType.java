package com.sparta.hotdeal.coupon.domain.entity;

import lombok.Getter;

@Getter
public enum CouponType {
    FIRST_COME_FIRST_SERVE("선착순 쿠폰"),
    DAILY_COUPON("데일리 쿠폰");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }
}
