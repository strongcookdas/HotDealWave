package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPutCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import com.sparta.hotdeal.coupon.application.mapper.CouponInfoMapper;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import com.sparta.hotdeal.coupon.domain.repository.CouponInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponInfoService {

    private final CouponInfoRepository couponInfoRepository;


    // 쿠폰 생성
    public ResPostCouponInfosDto createCoupon(ReqPostCouponInfosDto reqDto) {
        CouponInfo couponInfo = CouponInfoMapper.toEntity(reqDto);
        CouponInfo savedCouponInfo = couponInfoRepository.save(couponInfo);

        return ResPostCouponInfosDto.builder()
                .couponId(savedCouponInfo.getId())
                .build();
    }

    // 발급 쿠폰 상세 조회
    public ResGetCouponInfosByIdDto getCouponDetail(UUID couponInfoId) {
        CouponInfo couponInfo = couponInfoRepository.findById(couponInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COUPONINFO));

        return CouponInfoMapper.toResGetCouponInfosByIdDto(couponInfo);
    }

    // 발급 쿠폰 상태 변경
    @Transactional
    public void updateCouponStatus(UUID couponInfoId, CouponStatus status) {
        CouponInfo couponInfo = couponInfoRepository.findById(couponInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COUPONINFO));

        if (couponInfo.getStatus() == status) {
            throw new CustomException(ErrorCode.ALREADY_SET_COUPONINFO_STATUS);
        }
        couponInfo.updateStatus(status);
    }

    // 쿠폰 수정
    @Transactional
    public void updateCoupon(UUID couponInfoId, ReqPutCouponInfosByIdDto reqDto) {
        CouponInfo couponInfo = couponInfoRepository.findById(couponInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COUPONINFO));

        couponInfo.update(
                reqDto.getName(),
                reqDto.getQuantity(),
                reqDto.getDiscountAmount(),
                reqDto.getMinOrderAmount(),
                reqDto.getExpirationDate(),
                reqDto.getCouponType(),
                reqDto.getCompanyId()
        );
    }
}
