package com.sparta.hotdeal.coupon.infrastructure.repository;

import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponInfoRepositoryCustom {
    Page<CouponInfo> findCouponInfoWithSearchAndPaging(Pageable pageable, String search, CouponStatus couponStatus);
}
