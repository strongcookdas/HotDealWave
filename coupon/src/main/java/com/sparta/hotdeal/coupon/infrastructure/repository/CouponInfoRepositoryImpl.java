package com.sparta.hotdeal.coupon.infrastructure.repository;

import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.repository.CouponInfoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponInfoRepositoryImpl extends JpaRepository<CouponInfo, UUID>, CouponInfoRepository, CouponInfoRepositoryCustom {
}
