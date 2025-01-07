package com.sparta.hotdeal.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "coupon-service")
public interface CouponClient {
    // 구현 예정
}
