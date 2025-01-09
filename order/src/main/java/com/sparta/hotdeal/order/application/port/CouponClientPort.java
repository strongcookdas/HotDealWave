package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CouponClientPort {

    void useCoupon(UUID couponId);

    void recoverCoupon(UUID couponId);

    CouponValidationDto validateCoupon(UUID couponId, List<Basket> basketList, Map<UUID, ProductDto> productDtoMap);
}
