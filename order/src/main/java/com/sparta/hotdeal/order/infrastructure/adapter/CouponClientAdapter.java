package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.coupon.CouponDto;
import com.sparta.hotdeal.order.application.port.CouponClientPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponClientAdapter implements CouponClientPort {
    @Override
    public CouponDto getCoupon(UUID couponId) {
        if (couponId == null) {
            return null;
        }
        return CouponDto.createDummy();
    }

    @Override
    public void useCoupon(UUID couponId) {
        // 구현
    }

    @Override
    public void recoverCoupon(UUID couponId) {
        // 구현
    }
}
