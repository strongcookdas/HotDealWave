package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.coupon.CouponDto;
import java.util.UUID;

public interface CouponClientPort {
    CouponDto getCoupon(UUID couponId);
    void useCoupon(UUID couponId);
    void recoverCoupon(UUID couponId);
}
