package com.sparta.hotdeal.coupon.domain.entity;

import lombok.Getter;

@Getter
public enum CouponStatus {
    PENDING("발급 대기"),
    ISSUED("발급 중"),
    OUT_OF_STOCK("발급 불가");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }
}