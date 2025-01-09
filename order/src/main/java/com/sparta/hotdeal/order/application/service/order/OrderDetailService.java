package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.coupon.CouponDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    //회사 id에 따른 구매 상품 가격 계산 (쿠폰 유효성 검사를 위해)
    public Map<UUID, Integer> calculateTotalAmountByCompany(List<Basket> basketList,
                                                            Map<UUID, ProductDto> productMap) {

        Map<UUID, Integer> totalAmountByCompany = new HashMap<>();

        for (Basket basket : basketList) {
            ProductDto product = getProductOrThrow(productMap, basket.getProductId());
            validateProductForPurchase(product, basket.getQuantity());

            int productPrice = calculateProductPrice(product, basket.getQuantity());
            totalAmountByCompany.merge(product.getCompanyId(), productPrice, Integer::sum);
        }

        return totalAmountByCompany;
    }

    private ProductDto getProductOrThrow(
            Map<UUID, ProductDto> productMap,
            UUID productId) {
        return Optional.ofNullable(productMap.get(productId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));
    }

    private void validateProductForPurchase(ProductDto product, int quantity) {
        if (!"ON_SALE".equals(product.getStatus())) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_ON_SALE_EXCEPTION);
        } else if (product.getQuantity() < quantity) {
            throw new ApplicationException(ErrorCode.PRODUCT_INVALID_QUANTITY_EXCEPTION);
        }
    }

    private int calculateProductPrice(ProductDto product, int quantity) {
        int unitPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();
        return unitPrice * quantity;
    }

    //Map을 활용한 총 금액 계산
    public long calculateTotalAmountFromCompanyMap(Map<UUID, Integer> totalAmountByCompany) {
        // Map의 값들(Integer)을 합산하여 총 금액 계산
        return totalAmountByCompany.values().stream()
                .mapToLong(Integer::longValue) // Integer 값을 long으로 변환
                .sum();
    }

    public void validateCouponWithCompany(CouponDto coupon,
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
}
