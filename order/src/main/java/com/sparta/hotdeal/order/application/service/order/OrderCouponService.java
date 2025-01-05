package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.service.client.CouponClientService;
import com.sparta.hotdeal.order.infrastructure.exception.ApplicationException;
import com.sparta.hotdeal.order.infrastructure.exception.ErrorCode;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Order-Coupon]")
public class OrderCouponService {

    private final CouponClientService couponClientService;

    public ResGetCouponForOrderDto validateCoupon(UUID couponId) {
        if (couponId == null) {
            return null;
        }
        ResGetCouponForOrderDto coupon = couponClientService.getUserCoupon(couponId);
        if (!coupon.getExpirationDate().isAfter(LocalDate.now())) {
            log.error("쿠폰이 만료되었습니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        return coupon;
    }
}
