package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.service.client.CouponClientService;
import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Order-Coupon]")
public class OrderCouponService {

    private final CouponClientService couponClientService;

    public ResGetCouponForOrderDto getCoupon(UUID couponId) {
        if (couponId == null) {
            return null;
        }
        ResGetCouponForOrderDto coupon = couponClientService.getUserCoupon(couponId);
        if (!coupon.getExpirationDate().isAfter(LocalDate.now())) {
            throw new ApplicationException(ErrorCode.COUPON_EXPIRED_EXCEPTION);
        }
        return coupon;
    }

    public void validateCouponWithCompany(ResGetCouponForOrderDto coupon,
                                          Map<UUID, Integer> totalAmountByCompanyMap, long totalAmount) {
        if (coupon == null) {
            return;
        }

        if(coupon.getIsUsed()){
            throw new ApplicationException(ErrorCode.COUPON_ALREADY_USED_EXCEPTION);
        }

        // 쿠폰이 특정 회사에 제한되지 않은 경우 (companyId가 null)
        if (coupon.getCompanyId() == null) {
            // 최종 금액이 쿠폰 사용 금액대인지 체크
            if (totalAmount < coupon.getMinOrderAmount()) {
                throw new ApplicationException(ErrorCode.COUPON_MINIMUM_PRICE_EXCEPTION);
            }
            return; // 검증 완료
        }

        // 쿠폰이 특정 회사에 제한된 경우
        Integer companyTotalAmount = totalAmountByCompanyMap.get(coupon.getCompanyId());
        if (companyTotalAmount == null) {
            throw new ApplicationException(ErrorCode.COUPON_INVALID_COMPANY_EXCEPTION);
        }

        if (companyTotalAmount < coupon.getMinOrderAmount()) {
            throw new ApplicationException(ErrorCode.COUPON_MINIMUM_PRICE_EXCEPTION);
        }
    }

    public void useCoupon(UUID couponId) {
        couponClientService.useCoupon(couponId);
    }

    public void recoverCoupon(UUID couponId) {
        couponClientService.recoverCoupon(couponId);
    }

}
