package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.service.client.CouponClientService;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "coupon-service")
public interface CouponClient extends CouponClientService {
    @Override
    default ResGetCouponForOrderDto getUserCoupon(UUID couponId) {
        return ResGetCouponForOrderDto.createDummy();
    }
}
