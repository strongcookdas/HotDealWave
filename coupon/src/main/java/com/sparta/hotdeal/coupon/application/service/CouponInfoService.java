package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.mapper.CouponInfoMapper;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.repository.CouponInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponInfoService {

    private final CouponInfoRepository couponInfoRepository;


    public ResPostCouponInfosDto createCoupon(ReqPostCouponInfosDto reqDto) {
        CouponInfo couponInfo = CouponInfoMapper.toEntity(reqDto);
        CouponInfo savedCouponInfo = couponInfoRepository.save(couponInfo);

        return ResPostCouponInfosDto.builder()
                .couponId(savedCouponInfo.getId())
                .build();
    }


}
