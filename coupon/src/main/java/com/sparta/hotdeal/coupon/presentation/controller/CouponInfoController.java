package com.sparta.hotdeal.coupon.presentation.controller;

import com.sparta.hotdeal.coupon.application.dto.ResponseDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPatchCouponInfosByIdStatusDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPutCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.service.CouponInfoService;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import com.sparta.hotdeal.coupon.infrastructure.custom.RequestUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupon-infos")
@RequiredArgsConstructor
public class CouponInfoController {

    private final CouponInfoService couponInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    public ResponseDto<ResPostCouponInfosDto> createCoupon(@RequestBody ReqPostCouponInfosDto reqDto) {
        ResPostCouponInfosDto responseDto = couponInfoService.createCoupon(reqDto);
        return ResponseDto.of("쿠폰 정보 생성 성공", responseDto);
    }

    @PutMapping("/{couponInfoId}")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    public ResponseDto<Void> updateCoupon(@PathVariable UUID couponInfoId, @RequestBody ReqPutCouponInfosByIdDto reqDto) {
        couponInfoService.updateCoupon(couponInfoId, reqDto);
        return ResponseDto.of("쿠폰 정보 수정 성공", null);
    }

    @PatchMapping("/{couponInfoId}/status")
    @Secured({"ROLE_MASTER", "ROLE_MANAGER"})
    public ResponseDto<Void> updateCouponStatus(@PathVariable UUID couponInfoId, @RequestBody ReqPatchCouponInfosByIdStatusDto reqDto) {
        couponInfoService.updateCouponStatus(couponInfoId, reqDto.getStatus());
        return ResponseDto.of("쿠폰 정보의 상태 변경 성공", null);
    }

    @GetMapping("/{couponInfoId}")
    public ResponseDto<ResGetCouponInfosByIdDto> getCouponDetail(@PathVariable UUID couponInfoId) {
        ResGetCouponInfosByIdDto responseDto = couponInfoService.getCouponDetail(couponInfoId);
        return ResponseDto.of("쿠폰 정보 상세 조회 성공", responseDto);
    }

    @GetMapping
    public ResponseDto<Page<ResGetCouponInfosDto>> getCoupons(
            @AuthenticationPrincipal RequestUserDetails userDetails,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) CouponStatus couponStatus
    ) {
        if (userDetails.getRole().equals("CUSTOMER") || userDetails.getRole().equals("SELLER")) {
            couponStatus = CouponStatus.ISSUED;
        }

        Page<ResGetCouponInfosDto> couponInfoPage = couponInfoService.getCoupons(pageNumber, pageSize, sortBy, direction, search, couponStatus);

        return ResponseDto.of("쿠폰 정보 목록 조회 성공", couponInfoPage);
    }

}
