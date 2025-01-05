package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.service.client.CouponClientService;
import com.sparta.hotdeal.order.infrastructure.exception.ApplicationException;
import com.sparta.hotdeal.order.infrastructure.exception.ErrorCode;
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
            log.error("쿠폰이 만료되었습니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        return coupon;
    }

    public void validateCouponWithCompany(ResGetCouponForOrderDto coupon,
                                          Map<UUID, Integer> totalAmountByCompanyMap, long totalAmount) {
        if (coupon == null) {
            return;
        }

        // 쿠폰이 특정 회사에 제한되지 않은 경우 (companyId가 null)
        if (coupon.getCompanyId() == null) {
            // 모든 회사의 총 구매 금액 확인
            if (totalAmount < coupon.getMinOrderAmount()) {
                log.error("쿠폰을 사용할 수 있는 최소 금액이 아닙니다.");
                throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
            }
            return; // 검증 완료
        }

        // 쿠폰이 특정 회사에 제한된 경우
        Integer companyTotalAmount = totalAmountByCompanyMap.get(coupon.getCompanyId());
        if (companyTotalAmount == null) {
            log.error("해당 회사에서 사용할 수 없는 쿠폰입니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }

        if (companyTotalAmount < coupon.getMinOrderAmount()) {
            log.error("쿠폰을 사용할 수 있는 최소 금액이 아닙니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
    }

    public void useCoupon(UUID couponId) {
        couponClientService.useCoupon(couponId);
    }

    public void recoverCoupon(UUID couponId) {
        couponClientService.recoverCoupon(couponId);
    }

}
