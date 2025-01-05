package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.infrastructure.exception.ApplicationException;
import com.sparta.hotdeal.order.infrastructure.exception.ErrorCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Order-Product]")
public class OrderProductService {
    private final ProductClientService productClientService;

    public Map<UUID, ResGetProductListForOrderDto> getProductMap(List<Basket> basketList) {
        List<UUID> productIds = basketList.stream()
                .map(Basket::getProductId)
                .toList();

        List<ResGetProductListForOrderDto> productList = productClientService.getProductListForOrder(productIds);
        return productList.stream()
                .collect(Collectors.toMap(ResGetProductListForOrderDto::getProductId, product -> product));
    }

    public long calculateTotalAmount(
            List<Basket> basketList,
            Map<UUID, ResGetProductListForOrderDto> productMap,
            ResGetCouponForOrderDto coupon) {

        Map<UUID, Integer> totalAmountByCompany = new HashMap<>();
        long totalAmount = basketList.stream()
                .mapToLong(basket -> calculateBasketAmount(basket, productMap, totalAmountByCompany))
                .sum();

        if (coupon != null) {
            validateCouponWithCompany(coupon, totalAmountByCompany);
            totalAmount -= coupon.getDiscountAmount();
        }

        return totalAmount;
    }

    private long calculateBasketAmount(
            Basket basket,
            Map<UUID, ResGetProductListForOrderDto> productMap,
            Map<UUID, Integer> totalAmountByCompany) {

        ResGetProductListForOrderDto product = getProductOrThrow(productMap, basket.getProductId());
        validateProductForPurchase(product, basket.getQuantity());

        int productPrice = calculateProductPrice(product, basket.getQuantity());
        totalAmountByCompany.merge(product.getCompanyId(), productPrice, Integer::sum);
        return productPrice;
    }

    private ResGetProductListForOrderDto getProductOrThrow(
            Map<UUID, ResGetProductListForOrderDto> productMap,
            UUID productId) {
        return Optional.ofNullable(productMap.get(productId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    private void validateProductForPurchase(ResGetProductListForOrderDto product, int quantity) {
        if (!"ON_SALE".equals(product.getStatus())) {
            log.error("판매 중인 상품이 아닙니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }else if(product.getQuantity() < quantity){
            log.error("수량이 부족합니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
    }

    private int calculateProductPrice(ResGetProductListForOrderDto product, int quantity) {
        int unitPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();
        return unitPrice * quantity;
    }

    private void validateCouponWithCompany(
            ResGetCouponForOrderDto coupon,
            Map<UUID, Integer> totalAmountByCompany) {

        if (!totalAmountByCompany.containsKey(coupon.getCompanyId())) {
            log.error("");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }else if( totalAmountByCompany.get(coupon.getCompanyId()) < coupon.getMinOrderAmount()){
            log.error("쿠폰을 사용할 수 있는 최소 금액이 아닙니다.");
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
    }

    public List<ReqProductReduceQuantityDto> prepareProductReduction(
            List<Basket> basketList,
            Map<UUID, ResGetProductListForOrderDto> productMap) {

        return basketList.stream()
                .map(basket -> {
                    ResGetProductListForOrderDto product = getProductOrThrow(productMap, basket.getProductId());
                    return ReqProductReduceQuantityDto.of(product.getProductId(), basket.getQuantity());
                })
                .toList();
    }

    public void reduceProductQuantity(List<ReqProductReduceQuantityDto> reductionRequests) {
        productClientService.reduceProductQuantity(reductionRequests);
    }
}
