package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
import com.sparta.hotdeal.order.application.dtos.coupon.res.ResGetCouponForOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ProductDto;
import com.sparta.hotdeal.order.application.dtos.user.ResGetUserByIdForOrderDto;
import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import com.sparta.hotdeal.order.application.service.client.UserClientService;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClientService userClientService;

    private final OrderBasketService orderBasketService;
    private final OrderProductService orderProductService;
    private final OrderCouponService orderCouponService;

    public void createOrder(UUID userId, ReqPostOrderDto req) {
        //1. 장바구니 조회
        List<Basket> basketList = orderBasketService.getBasketList(req.getBasketList());
        //2. 상품 정보 조회
        Map<UUID, ProductDto> productMap = orderProductService.getProductMap(basketList);

        //3. 회사별 구매 상품 계산
        Map<UUID, Integer> totalAmountByCompanyMap = orderProductService.calculateTotalAmountByCompany(basketList,
                productMap);
        //4. 총 금액 계산
        long totalAmount = orderProductService.calculateTotalAmountFromCompanyMap(totalAmountByCompanyMap);

        //5. 쿠폰 유효성 및 쿠폰 적용
        ResGetCouponForOrderDto coupon = orderCouponService.getCoupon(req.getCouponId());
        orderCouponService.validateCouponWithCompany(coupon, totalAmountByCompanyMap, totalAmount);
        if (coupon != null) {
            orderCouponService.useCoupon(coupon.getCouponId());
        }

        //6. 상품 감소 처리
        orderProductService.reduceProductQuantity(basketList, productMap);
        //7. 장바구니 삭제
        orderBasketService.deleteBasketList(basketList);

        //8. 주문 저장 및 주문-상품 저장
        ResGetAddressByIdForOrderDto address = userClientService.getAddress(req.getAddressId());
        Order order = Order.create(
                address.getAddressId(),
                userId,
                totalAmount,
                (coupon == null) ? null : coupon.getCouponId(),
                (coupon == null) ? 0 : coupon.getDiscountAmount()
        );

        order = orderRepository.save(order);
        orderProductService.createOrderProductList(order, basketList, productMap);
        //결제 비동기 처리 추후 구현 필요
    }

    @Transactional(readOnly = true)
    public ResGetOrderByIdDto getOrderDetail(UUID userId, UUID orderId) {
        //주문 조회
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        //주소 조회
        ResGetAddressByIdForOrderDto address = userClientService.getAddress(order.getAddressId());

        //user 조회
        ResGetUserByIdForOrderDto user = userClientService.getUserById(order.getUserId());

        //order-product 조회
        List<OrderProductDto> orderProductDtoList = orderProductService.getOrderProductList(orderId);

        //product 조회
        Map<UUID, ProductDto> productMap = orderProductService.getProductMapByOrderProduct(
                orderProductDtoList);

        //조합 후 반환
        return ResGetOrderByIdDto.of(order, address, orderProductDtoList, productMap, user);
    }

    @Transactional(readOnly = true)
    public Page<ResGetOrderListDto> getOrderList(UUID userId, Pageable pageable) {
        //주문 내역 조회
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);

        //각 주문의 OrderProduct 데이터 가져오기
        List<UUID> orderIds = orderPage.stream()
                .map(Order::getId)
                .toList();
        Map<UUID, List<OrderProductDto>> orderProductMap = orderProductService.getOrderProductsByOrderIds(orderIds);

        //각 주문의 Product 가져오기
        List<UUID> productIds = orderProductMap.values().stream()
                .flatMap(List::stream)
                .map(OrderProductDto::getProductId)
                .toList();

        Map<UUID, ProductDto> productMap =orderProductService.getProductMapByProductIds(productIds);

        // 4. ResGetOrderListDto 생성
        return orderPage.map(order -> ResGetOrderListDto.of(
                order,
                orderProductMap.get(order.getId()),
                productMap
        ));
    }

}
