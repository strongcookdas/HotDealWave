package com.sparta.hotdeal.coupon.application.scheduler;

import com.sparta.hotdeal.coupon.application.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponExpirationScheduler {

    private final SchedulerService schedulerService;

    // 매일 00시에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        log.info("스케줄러 시작: 만료된 쿠폰 정보를 처리합니다...");
        schedulerService.processExpiredCoupons();
        log.info("스케줄러 종료: 만료된 쿠폰 정보 처리를 완료했습니다.");
    }
}
