package com.sparta.hotdeal.coupon.presentation.controller;

import com.sparta.hotdeal.coupon.application.dto.req.*;
import com.sparta.hotdeal.coupon.application.dto.res.*;
import com.sparta.hotdeal.coupon.application.dto.ResponseDto;
import com.sparta.hotdeal.coupon.application.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> issueFirstComeFirstServeCoupon(@RequestBody ReqPostCouponsIssueDto reqDto) {
        return ResponseDto.of("선착순 쿠폰 발급 성공", null);
    }

    @GetMapping
    public ResponseDto<List<ResGetUserCouponsDto>> getUserCoupons() {
        List<ResGetUserCouponsDto> responseDto = List.of(
                ResGetUserCouponsDto.builder()
                        .couponId(UUID.randomUUID())
                        .name("신규 가입 쿠폰")
                        .discountAmount(5000)
                        .minOrderAmount(10000)
                        .expirationDate(LocalDate.now().plusDays(30))
                        .isUsed(false)
                        .usedDate(null)
                        .dailyIssuedDate(LocalDate.now())
                        .build()
        );

        return ResponseDto.of("사용자 쿠폰 조회 성공", responseDto);
    }

    @PostMapping("/{couponId}/validate")
    public ResponseDto<ResPostCouponValidateDto> validateCoupon(@PathVariable UUID couponId, @RequestBody ReqPostCouponValidateDto reqDto) {
        ResPostCouponValidateDto responseDto = ResPostCouponValidateDto.builder()
                .isValid(true)
                .build();

        return ResponseDto.of("쿠폰 유효성 검사 성공", responseDto);
    }

    @PostMapping("/{couponId}/use")
    public ResponseDto<Void> useCoupon(@PathVariable UUID couponId) {
        return ResponseDto.of("쿠폰 사용 성공", null);
    }

    @PostMapping("/{couponId}/recover")
    public ResponseDto<Void> recoverCoupon(@PathVariable UUID couponId) {
        return ResponseDto.of("쿠폰 회복 성공", null);
    }

    @DeleteMapping("/{couponId}")
    public ResponseDto<Void> deleteCoupon(@PathVariable UUID couponId) {
        return ResponseDto.of("쿠폰 삭제 성공", null);
    }

    @GetMapping("/daily/statistics")
    public ResponseDto<ResGetDailyCouponStatisticsDto> getDailyCouponStatistics(@RequestParam String date) {
        ResGetDailyCouponStatisticsDto responseDto = ResGetDailyCouponStatisticsDto.builder()
                .totalUsers(100)
                .averageDiscount(500)
                .totalDiscount(50000)
                .build();

        return ResponseDto.of("데일리 쿠폰 통계 조회 성공", responseDto);
    }

    @PostMapping("/daily/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> issueDailyCoupon() {
        return ResponseDto.of("데일리 쿠폰 발급 성공", null);
    }
}
