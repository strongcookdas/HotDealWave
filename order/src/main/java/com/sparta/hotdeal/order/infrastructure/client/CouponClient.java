package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.infrastructure.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.dtos.coupon.req.ReqPostCouponValidateDto;
import com.sparta.hotdeal.order.infrastructure.dtos.coupon.res.ResPostCouponValidateDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "coupon-service", configuration = CouponClient.class)
public interface CouponClient {
    @PostMapping("/{couponId}/validate")
    ResponseDto<ResPostCouponValidateDto> validateCoupon(@PathVariable UUID couponId,
                                                         @RequestBody ReqPostCouponValidateDto reqPostCouponValidateDto);
    @PostMapping("/{couponId}/use")
    ResponseDto<Void> useCoupon(@PathVariable UUID couponId);

    @PostMapping("/{couponId}/recover")
    ResponseDto<Void> recoverCoupon(@PathVariable UUID couponId);
}
