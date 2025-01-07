package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponInfoRepository extends JpaRepository<CouponInfo, UUID> {
    Optional<CouponInfo> findByIdAndIsDeletedFalse(UUID couponInfoId);
    List<CouponInfo> findAllByExpirationDateBeforeAndIsDeletedFalse(LocalDate expirationDate);
}
