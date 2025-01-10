package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPutCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import com.sparta.hotdeal.coupon.application.mapper.CouponInfoMapper;
import com.sparta.hotdeal.coupon.application.service.client.CompanyClientService;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import com.sparta.hotdeal.coupon.domain.repository.CouponInfoRepository;
import com.sparta.hotdeal.coupon.infrastructure.dto.ResGetCompanyByIdDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponInfoService {

    private final CouponInfoRepository couponInfoRepository;
    private final CompanyClientService companyClientService;


    // 쿠폰 생성
    public ResPostCouponInfosDto createCoupon(ReqPostCouponInfosDto reqDto) {
        ResGetCompanyByIdDto companyResponse = companyClientService.getCompanyDataById(reqDto.getCompanyId());

        if (!"APPROVED".equalsIgnoreCase(companyResponse.getStatus())) {
            throw new CustomException(ErrorCode.COMPANY_NOT_APPROVED);
        }

        CouponInfo couponInfo = CouponInfoMapper.toEntity(reqDto);
        CouponInfo savedCouponInfo = couponInfoRepository.save(couponInfo);

        return ResPostCouponInfosDto.builder()
                .couponId(savedCouponInfo.getId())
                .build();
    }

    // 발급 쿠폰 상세 조회
    public ResGetCouponInfosByIdDto getCouponDetail(UUID couponInfoId) {
        CouponInfo couponInfo = findByIdOrThrow(couponInfoId);

        return CouponInfoMapper.toResGetCouponInfosByIdDto(couponInfo);
    }

    // 발급 쿠폰 상태 변경
    @Transactional
    public void updateCouponStatus(UUID couponInfoId, CouponStatus status) {
        CouponInfo couponInfo = findByIdOrThrow(couponInfoId);

        if (couponInfo.getStatus() == status) {
            throw new CustomException(ErrorCode.ALREADY_SET_COUPON_INFO_STATUS);
        }
        couponInfo.updateStatus(status);
    }

    // 쿠폰 수정
    @Transactional
    public void updateCoupon(UUID couponInfoId, ReqPutCouponInfosByIdDto reqDto) {
        CouponInfo couponInfo = findByIdOrThrow(couponInfoId);

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

    public CouponInfo findByIdOrThrow(UUID couponInfoId) {
        return couponInfoRepository.findByIdAndDeletedAtIsNull(couponInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COUPON_INFO));
    }
}
