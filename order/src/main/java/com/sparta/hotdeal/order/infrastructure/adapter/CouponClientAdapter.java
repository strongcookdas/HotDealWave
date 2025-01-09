package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.coupon.CouponDto;
import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.port.CouponClientPort;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.infrastructure.client.CouponClient;
import com.sparta.hotdeal.order.infrastructure.dtos.coupon.req.ReqPostCouponValidateDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponClientAdapter implements CouponClientPort {
    private final CouponClient couponClient;

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

    @Override
    public CouponValidationDto validateCoupon(UUID couponId, List<Basket> basketList,
                                              Map<UUID, ProductDto> productDtoMap) {

        List<ReqPostCouponValidateDto.Product> productList = basketList.stream().flatMap(basket -> {

            ProductDto productDto = Optional.ofNullable(productDtoMap.get(basket.getProductId())).orElseThrow(
                    () -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

            return IntStream.range(0, basket.getQuantity())
                    .mapToObj(i -> ReqPostCouponValidateDto.Product.create(productDto));
        }).toList();

        ReqPostCouponValidateDto reqPostCouponValidateDto = ReqPostCouponValidateDto.create(productList);
        return couponClient.validateCoupon(couponId, reqPostCouponValidateDto).getData().toCouponValidationDto();
    }
}
