package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.Coupon;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    boolean existsByUserIdAndCouponInfoId(UUID userId, UUID couponInfoId);

    Optional<Coupon> findByIdAndIsDeletedFalse(UUID couponId);

    List<Coupon> findByUserIdAndIsDeletedFalseAndIsUsed(UUID userId, boolean isUsed);

    List<Coupon> findAllByCouponInfoAndIsDeletedFalse(CouponInfo couponInfo);
}
