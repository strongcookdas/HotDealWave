package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.Coupon;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository {
    boolean existsByUserIdAndCouponInfoId(UUID userId, UUID couponInfoId);

    Optional<Coupon> findByIdAndIsDeletedFalse(UUID couponId);

    List<Coupon> findByUserIdAndIsDeletedFalseAndIsUsed(UUID userId, boolean isUsed);

    List<Coupon> findAllByCouponInfoAndIsDeletedFalse(CouponInfo couponInfo);

    Coupon save(Coupon coupon);
}
