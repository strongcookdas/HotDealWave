package com.sparta.hotdeal.coupon.presentation.controller;

import com.sparta.hotdeal.coupon.application.dto.ResponseDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponValidateDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponsIssueDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetDailyCouponStatisticsDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetUserCouponsDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponValidateDto;
import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import com.sparta.hotdeal.coupon.application.service.CouponService;
import com.sparta.hotdeal.coupon.infrastructure.custom.RequestUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> issueFirstComeFirstServeCoupon(
            @RequestHeader(value = "Test-UserId", required = false) UUID testUserId,
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestBody ReqPostCouponsIssueDto reqDto) {
        // 운영 환경에서는 testUserId를 사용하지 않음
        UUID userId = (testUserId != null) ? testUserId : userDetails.getUserId();
        couponService.issueFirstComeFirstServeCoupon(userId, reqDto);
        return ResponseDto.of("선착순 쿠폰 발급 성공", null);
    }


    @GetMapping
    public ResponseDto<List<ResGetUserCouponsDto>> getUserCoupons(
            @RequestParam(defaultValue = "false") boolean isUsed,
            @RequestParam UUID userId,
            @AuthenticationPrincipal RequestUserDetails userDetails) {

        if (userDetails.getRole().equals("CUSTOMER") || userDetails.getRole().equals("SELLER")) {
            if (!userDetails.getUserId().equals(userId)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
            }
        }

        List<ResGetUserCouponsDto> responseDto = couponService.getUserCoupons(userId, isUsed);

        return ResponseDto.of("사용자 쿠폰 목록 조회 성공", responseDto);
    }

    @GetMapping("/{couponId}")
    public ResponseDto<ResGetUserCouponsDto> getUserCouponDetail(@PathVariable UUID couponId) {
        ResGetUserCouponsDto responseDto = couponService.getUserCouponDetail(couponId);
        return ResponseDto.of("사용자 쿠폰 상세 조회 성공", responseDto);
    }


    @PostMapping("/{couponId}/validate")
    public ResponseDto<ResPostCouponValidateDto> validateCoupon(@PathVariable UUID couponId,
                                                                @RequestBody ReqPostCouponValidateDto reqDto) {
        ResPostCouponValidateDto responseDto = couponService.validateCoupon(couponId, reqDto);
        return ResponseDto.of("쿠폰 유효성 검사 성공", responseDto);
    }


    @PostMapping("/{couponId}/use")
    @Secured({"ROLE_MASTER"})
    public ResponseDto<Void> useCoupon(@PathVariable UUID couponId) {
        couponService.useCoupon(couponId);
        return ResponseDto.of("쿠폰 사용 성공", null);
    }

    @PostMapping("/{couponId}/recover")
    @Secured({"ROLE_MASTER"})
    public ResponseDto<Void> recoverCoupon(@PathVariable UUID couponId) {
        couponService.recoverCoupon(couponId);
        return ResponseDto.of("쿠폰 회복 성공", null);
    }

    @DeleteMapping("/{couponId}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    public ResponseDto<Void> deleteCoupon(@PathVariable UUID couponId,
                                          @AuthenticationPrincipal RequestUserDetails userDetails) {
        couponService.deleteCoupon(couponId, userDetails.getEmail());
        return ResponseDto.of("사용자 쿠폰 삭제 성공", null);
    }

    // 추후 구현
    @GetMapping("/daily/statistics")
    public ResponseDto<ResGetDailyCouponStatisticsDto> getDailyCouponStatistics(@RequestParam String date) {
        ResGetDailyCouponStatisticsDto responseDto = ResGetDailyCouponStatisticsDto.builder()
                .totalUsers(100)
                .averageDiscount(500)
                .totalDiscount(50000)
                .build();

        return ResponseDto.of("데일리 쿠폰 통계 조회 성공", responseDto);
    }

    // 추후 구현
    @PostMapping("/daily/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> issueDailyCoupon() {
        return ResponseDto.of("데일리 쿠폰 발급 성공", null);
    }
}
