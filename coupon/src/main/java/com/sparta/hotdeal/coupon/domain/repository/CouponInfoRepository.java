package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponInfoRepository {
    Optional<CouponInfo> findByIdAndDeletedAtIsNull(UUID couponInfoId);

    List<CouponInfo> findAllByExpirationDateBeforeAndDeletedAtIsNull(LocalDate expirationDate);

    CouponInfo save(CouponInfo couponInfo);

    Page<CouponInfo> findCouponInfoWithSearchAndPaging(Pageable pageable, String search, CouponStatus couponStatus);
}
