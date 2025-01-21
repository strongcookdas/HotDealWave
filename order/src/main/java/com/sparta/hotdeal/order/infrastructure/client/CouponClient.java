package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.infrastructure.config.CouponClientConfig;
import com.sparta.hotdeal.order.infrastructure.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.dtos.coupon.req.ReqPostCouponValidateDto;
import com.sparta.hotdeal.order.infrastructure.dtos.coupon.res.ResPostCouponValidateDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "coupon-service", configuration = CouponClientConfig.class)
public interface CouponClient {
    @PostMapping("/api/v1/coupons/{couponId}/validate")
    ResponseDto<ResPostCouponValidateDto> validateCoupon(@PathVariable UUID couponId,
                                                         @RequestBody ReqPostCouponValidateDto reqPostCouponValidateDto);
    @PostMapping("/api/v1/coupons/{couponId}/use")
    ResponseDto<Void> useCoupon(@PathVariable UUID couponId);

    @PostMapping("/api/v1/coupons/{couponId}/recover")
    ResponseDto<Void> recoverCoupon(@PathVariable UUID couponId);
}
