package com.sparta.hotdeal.coupon.domain.repository;

import com.sparta.hotdeal.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
}
