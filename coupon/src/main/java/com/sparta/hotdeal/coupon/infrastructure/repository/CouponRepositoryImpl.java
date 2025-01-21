package com.sparta.hotdeal.coupon.infrastructure.repository;

import com.sparta.hotdeal.coupon.domain.entity.Coupon;
import com.sparta.hotdeal.coupon.domain.repository.CouponRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepositoryImpl extends JpaRepository<Coupon, UUID>, CouponRepository {
}
