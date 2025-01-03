package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.mapper.CouponInfoMapper;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.repository.CouponInfoRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 ID입니다: " + couponInfoId));

        return CouponInfoMapper.toResGetCouponInfosByIdDto(couponInfo);
    }

}
