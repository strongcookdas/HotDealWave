package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponInfoRepository extends JpaRepository<CouponInfo, UUID> {
}
