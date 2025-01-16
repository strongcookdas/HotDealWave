package com.sparta.hotdeal.order.infrastructure.adapter;

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
    public void useCoupon(UUID couponId) {
        couponClient.useCoupon(couponId);
    }

    @Override
    public void recoverCoupon(UUID couponId) {
        couponClient.recoverCoupon(couponId);
    }

    @Override
    public CouponValidationDto validateCoupon(UUID couponId, List<Basket> basketList,
                                              Map<UUID, ProductDto> productDtoMap) {
        if (couponId == null) {
            return CouponValidationDto.create(false, 0);
        }

        List<ReqPostCouponValidateDto.Product> productList = basketList.stream().map(basket -> {

                    ProductDto productDto = Optional.ofNullable(productDtoMap.get(basket.getProductId())).orElseThrow(
                            () -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

                    return ReqPostCouponValidateDto.Product.create(productDto, basket.getQuantity());
                }
        ).toList();

        ReqPostCouponValidateDto reqPostCouponValidateDto = ReqPostCouponValidateDto.create(productList);
        return couponClient.validateCoupon(couponId, reqPostCouponValidateDto).getData().toCouponValidationDto();
    }
}
