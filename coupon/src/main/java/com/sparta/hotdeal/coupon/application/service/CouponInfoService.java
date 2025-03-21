package com.sparta.hotdeal.coupon.application.service;

import com.sparta.hotdeal.coupon.application.dto.req.ReqPostCouponInfosDto;
import com.sparta.hotdeal.coupon.application.dto.req.ReqPutCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosByIdDto;
import com.sparta.hotdeal.coupon.application.dto.res.ResGetCouponInfosDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponInfoService {

    private final CouponInfoRepository couponInfoRepository;
    private final CompanyClientService companyClientService;


    // 쿠폰 생성
    public ResPostCouponInfosDto createCoupon(ReqPostCouponInfosDto reqDto) {
        if (reqDto.getCompanyId() != null) {
            ResGetCompanyByIdDto companyResponse = companyClientService.getCompanyDataById(reqDto.getCompanyId());

            if (!"APPROVED".equalsIgnoreCase(companyResponse.getStatus())) {
                throw new CustomException(ErrorCode.COMPANY_NOT_APPROVED);
            }
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
        if (reqDto.getCompanyId() != null) {
            ResGetCompanyByIdDto companyResponse = companyClientService.getCompanyDataById(reqDto.getCompanyId());

            if (!"APPROVED".equalsIgnoreCase(companyResponse.getStatus())) {
                throw new CustomException(ErrorCode.COMPANY_NOT_APPROVED);
            }
        }
        CouponInfo couponInfo = findByIdOrThrow(couponInfoId);

        if (couponInfo.getStatus() != CouponStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_COUPON_STATUS);
        }

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

    public Page<ResGetCouponInfosDto> getCoupons(int pageNumber, int pageSize, String sortBy, String direction,
                                                String search, CouponStatus couponStatus) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<CouponInfo> couponInfos = couponInfoRepository.findCouponInfoWithSearchAndPaging(pageable, search, couponStatus);

        return CouponInfoMapper.toResGetCouponInfosPage(couponInfos);
    }

    public CouponInfo findByIdOrThrow(UUID couponInfoId) {
        return couponInfoRepository.findByIdAndDeletedAtIsNull(couponInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COUPON_INFO));
    }
}
