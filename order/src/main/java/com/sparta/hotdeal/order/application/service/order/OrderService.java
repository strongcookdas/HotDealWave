package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.user.UserDto;
import com.sparta.hotdeal.order.application.port.CouponClientPort;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.application.port.UserClientPort;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final ProductClientPort productClientPort;
    private final CouponClientPort couponClientPort;
    private final UserClientPort userClientPort;

    private final OrderProductService orderProductService;
    private final OrderBasketService orderBasketService;

    public void createOrder(UUID userId, ReqPostOrderDto req) {
        //1. 장바구니 조회
        List<Basket> basketList = orderBasketService.getBasketList(req.getBasketList());

        //2. 상품 정보 조회
        List<UUID> productIds = basketList.stream().map(Basket::getProductId).toList();
        List<ProductDto> productDtoList = productClientPort.getProductALL(productIds);
        Map<UUID, ProductDto> productDtoMap = convertListToMap(productDtoList);

        if (basketList.size() != productDtoList.size()) {
            throw new ApplicationException(ErrorCode.ORDER_INVALID_VALUE_EXCEPTION);
        }

        //4. 총 금액 계산
        int totalAmount = calculateTotalAmount(basketList, productDtoMap);

        //5. 쿠폰 유효성 및 쿠폰 적용
        int discountAmount = 0;
        UUID couponId = null;
        CouponValidationDto couponValidationDto = couponClientPort.validateCoupon(req.getCouponId(), basketList, productDtoMap);

        if(couponValidationDto.isValid()){
            discountAmount = couponValidationDto.getTotalDiscountAmount();
            couponId = req.getCouponId();
            couponClientPort.useCoupon(couponId);
        }

        //6. 상품 감소 처리
//        productClientPort.reduceProductQuantity(basketList, productMap);

        //7. 장바구니 삭제
        orderBasketService.deleteBasketList(basketList);

        //8. 주문 저장 및 주문-상품 저장
        AddressDto address = userClientPort.getAddress(req.getAddressId());
        Order order = Order.create(
                address.getAddressId(),
                userId,
                totalAmount,
                getOrderName(productDtoList),
                couponId,
                discountAmount
        );

        order = orderRepository.save(order);
        orderProductService.createOrderProductList(order, basketList, productDtoList);
        //주문 생성 이후 비동리 처리 (쿠폰 사용, 수량 감소, 결제 요청)
    }

    @Transactional(readOnly = true)
    public ResGetOrderByIdDto getOrderDetail(UUID userId, UUID orderId) {
        //주문 조회
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        //주소 조회
        AddressDto address = userClientPort.getAddress(order.getAddressId());

        //user 조회
        UserDto user = userClientPort.getUserById(order.getUserId());

        //order-product 조회
        List<OrderProductDto> orderProductDtoList = orderProductService.getOrderProductList(orderId);

        //product 조회
        List<UUID> productIds = orderProductDtoList.stream().map(OrderProductDto::getProductId).toList();
        Map<UUID, ProductDto> productMap = productClientPort.getProductAll(productIds);

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

        Map<UUID, ProductDto> productMap = productClientPort.getProductAll(productIds);

        // 4. ResGetOrderListDto 생성
        return orderPage.map(order -> ResGetOrderListDto.of(
                order,
                orderProductMap.get(order.getId()),
                productMap
        ));
    }

    private int calculateTotalAmount(List<Basket> basketList, Map<UUID, ProductDto> productDtoMap) {
        int totalAmount = 0;
        for (Basket basket : basketList) {

            ProductDto productDto = Optional.ofNullable(productDtoMap.get(basket.getProductId()))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

            if (productDto.getQuantity() < basket.getQuantity()) {
                throw new ApplicationException(ErrorCode.PRODUCT_INVALID_QUANTITY_EXCEPTION);
            }

            int productPrice =
                    (productDto.getDiscountPrice() == null) ? productDto.getPrice() : productDto.getDiscountPrice();
            totalAmount += (productPrice * basket.getQuantity());
        }
        return totalAmount;
    }

    private String getOrderName(List<ProductDto> productDtoList) {
        int listSize = productDtoList.size();
        return productDtoList.get(0).getName() + "외 " + (listSize - 1) + "개";
    }

    private Map<UUID, ProductDto> convertListToMap(List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .collect(Collectors.toMap(ProductDto::getProductId, productDto -> productDto));
    }
}
