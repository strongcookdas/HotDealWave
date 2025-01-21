package com.sparta.hotdeal.coupon.application.mapper;

import com.sparta.hotdeal.coupon.application.dto.res.ResGetUserCouponsDto;
import com.sparta.hotdeal.coupon.domain.entity.Coupon;

import java.util.List;
import java.util.stream.Collectors;

public class CouponMapper {

    // 사용자의 쿠폰목록 조회 시
    public static List<ResGetUserCouponsDto> toResGetUserCouponsDtoList(List<Coupon> coupons) {
        return coupons.stream()
                .map(coupon -> ResGetUserCouponsDto.builder()
                        .couponId(coupon.getId())
                        .name(coupon.getCouponInfo().getName())
                        .discountAmount(coupon.getCouponInfo().getDiscountAmount())
                        .minOrderAmount(coupon.getCouponInfo().getMinOrderAmount())
                        .expirationDate(coupon.getCouponInfo().getExpirationDate())
                        .isUsed(coupon.isUsed())
                        .usedDate(coupon.getUsedDate())
                        .dailyIssuedDate(coupon.getDailyIssuedDate())
                        .build())
                .collect(Collectors.toList());
    }

    // 사용자의 쿠폰 상세 조회 시
    public static ResGetUserCouponsDto toResGetUserCouponsDto(Coupon coupon) {
        return ResGetUserCouponsDto.builder()
                .couponId(coupon.getId())
                .name(coupon.getCouponInfo().getName())
                .discountAmount(coupon.getCouponInfo().getDiscountAmount())
                .minOrderAmount(coupon.getCouponInfo().getMinOrderAmount())
                .expirationDate(coupon.getCouponInfo().getExpirationDate())
                .isUsed(coupon.isUsed())
                .usedDate(coupon.getUsedDate())
                .dailyIssuedDate(coupon.getDailyIssuedDate())
                .build();
    }
}
