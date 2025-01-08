package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.domain.entity.Coupon;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.repository.CouponInfoRepository;
import com.sparta.hotdeal.coupon.domain.repository.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final CouponInfoRepository couponInfoRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void processExpiredCoupons() {
        List<CouponInfo> expiredCouponInfos = couponInfoRepository.findAllByExpirationDateBeforeAndDeletedAtIsNull(LocalDate.now());

        expiredCouponInfos.forEach(couponInfo -> {
            List<Coupon> relatedCoupons = couponRepository.findAllByCouponInfoAndDeletedAtIsNull(couponInfo);
            relatedCoupons.forEach(Coupon::systemDelete);

            couponInfo.systemDelete();

            log.info("쿠폰 정보 ID: {}와 연관된 쿠폰 {}개를 소프트 삭제했습니다.",
                    couponInfo.getId(), relatedCoupons.size());
        });

        log.info("스케줄러: 총 {}개의 만료된 쿠폰 정보를 소프트 삭제했습니다.", expiredCouponInfos.size());
    }

}
