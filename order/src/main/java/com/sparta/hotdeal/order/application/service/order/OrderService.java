package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import com.sparta.hotdeal.order.application.service.client.CouponClientService;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import com.sparta.hotdeal.order.infrastructure.exception.ApplicationException;
import com.sparta.hotdeal.order.infrastructure.exception.ErrorCode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final ProductClientService productClientService;
    private final CouponClientService couponClientService;

    public void createOrder(ReqPostOrderDto req) {
        // 1. 장바구니 조회
        List<Basket> basketList = getBasketList(req.getBasketList());

        // 2. 상품 정보 조회
        Map<UUID, ResGetProductListForOrderDto> productMap = getProductMap(basketList);

        // 3. 쿠폰 검증
        ResGetCouponForOrderDto coupon = validateCoupon(req.getCouponId());

        // 4. 총 금액 계산
        long totalAmount = calculateTotalAmount(basketList, productMap, coupon);

        // 5. 상품 감소 요청 준비
        List<ReqProductReduceQuantityDto> reductionRequests = prepareProductReduction(basketList, productMap);

        // 6. 상품 감소 처리
        productClientService.reduceProductQuantity(reductionRequests);

        // 7. 결제 처리 (추후 구현)
        processPayment(totalAmount);
    }

    // 1. 장바구니 조회
    private List<Basket> getBasketList(List<UUID> basketIds) {
        List<Basket> baskets = basketRepository.findByIdIn(basketIds);
        if (baskets.isEmpty()) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        return baskets;
    }

    // 2. 상품 정보 조회
    private Map<UUID, ResGetProductListForOrderDto> getProductMap(List<Basket> basketList) {
        List<UUID> productIds = basketList.stream()
                .map(Basket::getProductId)
                .toList();

        List<ResGetProductListForOrderDto> productList = productClientService.getProductListForOrder(productIds);
        return productList.stream()
                .collect(Collectors.toMap(ResGetProductListForOrderDto::getProductId, product -> product));
    }

    // 3. 쿠폰 검증
    private ResGetCouponForOrderDto validateCoupon(UUID couponId) {
        if (couponId == null) {
            return null;
        }
        ResGetCouponForOrderDto coupon = couponClientService.getUserCoupon(couponId);
        if (!coupon.getExpirationDate().isAfter(LocalDate.now())) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        return coupon;
    }

    // 4. 총 금액 계산
    private long calculateTotalAmount(
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
        if (!"ON_SALE".equals(product.getStatus()) || product.getQuantity() < quantity) {
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

        if (!totalAmountByCompany.containsKey(coupon.getCompanyId()) ||
                totalAmountByCompany.get(coupon.getCompanyId()) < coupon.getMinOrderAmount()) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
    }

    // 5. 상품 감소 요청 준비
    private List<ReqProductReduceQuantityDto> prepareProductReduction(
            List<Basket> basketList,
            Map<UUID, ResGetProductListForOrderDto> productMap) {

        return basketList.stream()
                .map(basket -> {
                    ResGetProductListForOrderDto product = getProductOrThrow(productMap, basket.getProductId());
                    return ReqProductReduceQuantityDto.of(product.getProductId(), basket.getQuantity());
                })
                .toList();
    }

    // 7. 결제 처리
    private void processPayment(long totalAmount) {
        // 결제 처리 로직 (추후 구현)
        // 예: paymentService.processPayment(totalAmount);
    }
}
