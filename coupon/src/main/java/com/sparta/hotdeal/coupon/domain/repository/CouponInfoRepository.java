package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponInfoRepository {
    Optional<CouponInfo> findByIdAndIsDeletedFalse(UUID couponInfoId);

    List<CouponInfo> findAllByExpirationDateBeforeAndIsDeletedFalse(LocalDate expirationDate);

    CouponInfo save(CouponInfo couponInfo);
}
