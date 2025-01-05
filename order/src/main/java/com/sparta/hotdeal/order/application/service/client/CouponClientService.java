package com.sparta.hotdeal.order.application.service.client;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import java.util.UUID;

public interface CouponClientService {
    ResGetCouponForOrderDto getUserCoupon(UUID couponId);
    void useCoupon(UUID couponId);
    void recoverCoupon(UUID couponId);
}
