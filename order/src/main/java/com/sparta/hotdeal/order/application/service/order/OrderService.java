package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import com.sparta.hotdeal.order.application.service.client.AddressClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressClientService addressClientService;

    private final OrderBasketService orderBasketService;
    private final OrderProductService orderProductService;
    private final OrderCouponService orderCouponService;

    public void createOrder(UUID userId, ReqPostOrderDto req) {
        // 1. 장바구니 조회
        List<Basket> basketList = orderBasketService.getBasketList(req.getBasketList());
        // 2. 상품 정보 조회
        Map<UUID, ResGetProductListForOrderDto> productMap = orderProductService.getProductMap(basketList);

        // 3. 회사별 구매 상품 계산
        Map<UUID, Integer> totalAmountByCompanyMap = orderProductService.calculateTotalAmountByCompany(basketList,
                productMap);
        // 4. 총 금액 계산
        long totalAmount = orderProductService.calculateTotalAmountFromCompanyMap(totalAmountByCompanyMap);

        // 5. 쿠폰 유효성 및 쿠폰 적용
        ResGetCouponForOrderDto coupon = orderCouponService.getCoupon(req.getCouponId());
        orderCouponService.validateCouponWithCompany(coupon, totalAmountByCompanyMap, totalAmount);
        if (coupon != null) {
            totalAmount -= coupon.getDiscountAmount();
            orderCouponService.useCoupon(coupon.getCouponId());
        }

        // 6. 상품 감소 처리
        orderProductService.reduceProductQuantity(basketList, productMap);
        // 7. 장바구니 삭제
        orderBasketService.deleteBasketList(basketList);

        // 8. 주문 저장 및 주문-상품 저장
        ResGetAddressByIdForOrderDto address = addressClientService.getAddress(req.getAddressId());
        Order order = Order.create(
                address.getAddressId(),
                userId,
                totalAmount,
                (coupon==null)? null:coupon.getCouponId()
        );

        order = orderRepository.save(order);
        orderProductService.createOrderProductList(order,basketList,productMap);
    }


    // 7. 결제 처리
    private void processPayment(long totalAmount) {
        // 결제 처리 로직 (추후 구현)
        // 예: paymentService.processPayment(totalAmount);
    }
}
