package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponsIssueDto;
import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import com.sparta.hotdeal.coupon.domain.entity.Coupon;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import com.sparta.hotdeal.coupon.domain.repository.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponInfoService couponInfoService;
    private final CouponRepository couponRepository;

    @Transactional
    public void issueFirstComeFirstServeCoupon(UUID userId, ReqPostCouponsIssueDto reqDto) {
        // 1. 쿠폰 정보 조회
        CouponInfo couponInfo = couponInfoService.findByIdOrThrow(reqDto.getCouponInfoId());

        // 2. 유효성 검증: 쿠폰 상태 확인
        if (couponInfo.getStatus() != CouponStatus.ISSUED) {
            throw new CustomException(ErrorCode.INVALID_COUPON_STATUS);
        }

        // 3. 발급 가능 수량 확인
        if (couponInfo.getIssuedCount() >= couponInfo.getQuantity()) {
            throw new CustomException(ErrorCode.COUPON_OUT_OF_STOCK);
        }

        // 4. 사용자 중복 발급 확인
        boolean alreadyIssued = couponRepository.existsByUserIdAndCouponInfoId(userId, couponInfo.getId());
        if (alreadyIssued) {
            throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);
        }

        // 5. 쿠폰 발급 처리
        couponInfo.incrementIssuedCount();

        if (couponInfo.getIssuedCount() == couponInfo.getQuantity()) {
            couponInfo.updateStatus(CouponStatus.OUT_OF_STOCK);
        }

        // 6. 쿠폰 엔티티 저장
        Coupon coupon = Coupon.builder()
                .userId(userId)
                .couponInfo(couponInfo)
                .dailyIssuedDate(LocalDate.now())
                .isUsed(false)
                .usedDate(null)
                .build();
        couponRepository.save(coupon);
    }
}

