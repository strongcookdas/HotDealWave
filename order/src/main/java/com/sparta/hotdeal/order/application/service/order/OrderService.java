package com.sparta.hotdeal.order.application.service.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPutOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqPatchProductQuantityDto;
import com.sparta.hotdeal.order.application.dtos.user.UserDto;
import com.sparta.hotdeal.order.application.port.CouponClientPort;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.application.port.UserClientPort;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
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

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public ResPostOrderDto createOrder(UUID userId, String email, String role, ReqPostOrderDto req) {
        List<Basket> basketList = orderBasketService.getBasketList(userId, req.getBasketList());
        log.info("장바구니 조회");

        List<UUID> productIds = basketList.stream().map(Basket::getProductId).toList();
        List<ProductDto> productDtoList = productClientPort.getProductALL(productIds);
        log.info("상품 조회 정보");

        Map<UUID, ProductDto> productDtoMap = convertListToMap(productDtoList);
        log.info("productDto -> productMap");

        if (basketList.size() != productDtoList.size()) {
            log.error("상품과 장바구니 수량 불일치 예외 발생");
            throw new ApplicationException(ErrorCode.ORDER_INVALID_VALUE_EXCEPTION);
        }

        int totalAmount = calculateTotalAmount(basketList, productDtoMap);
        log.info("총 금액 계산");

        int discountAmount = 0;
        UUID couponId = null;
        CouponValidationDto couponValidationDto = couponClientPort.validateCoupon(req.getCouponId(), basketList,
                productDtoMap);
        log.info("쿠폰 유효성 체크 호출");

        if (couponValidationDto.isValid()) {
            discountAmount = couponValidationDto.getTotalDiscountAmount();
            couponId = req.getCouponId();
            couponClientPort.useCoupon(couponId);
            log.info("쿠폰 사용 api 호출");
        }

        orderBasketService.deleteBasketList(basketList);
        log.info("장바구니 삭제 처리");

        AddressDto address = userClientPort.getAddress(userId, email, role, req.getAddressId());
        log.info("주소 조회 api 호출");
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
        log.info("주문 정보 DB 저장");

        sendReduceProductQuantityMessage(order, basketList);

        return ResPostOrderDto.of(order.getId());
    }

    private void sendReduceProductQuantityMessage(Order order, List<Basket> basketList) {
        try {
            // User 객체를 JSON으로 변환 추후 직렬화 개선 필요
            ReqPatchProductQuantityDto req = ReqPatchProductQuantityDto.create(order.getId(), basketList);
            String reqJson = objectMapper.writeValueAsString(req);
            kafkaTemplate.send("reduce_quantity", reqJson);
        } catch (Exception e) {
            log.error("상품 수량 감소 직렬화 실패");
        }
    }

    @Transactional(readOnly = true)
    public ResGetOrderByIdDto getOrderDetail(UUID userId, String email, String role, UUID orderId) {
        //주문 조회
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));
        log.info("주문 조회");

        //주소 조회
        AddressDto address = userClientPort.getAddress(userId, email, role, order.getAddressId());
        log.info("주소 조회");

        //user 조회
        UserDto user = userClientPort.getUserById(userId, email, role);
        log.info("유저 조회");

        //order-product 조회
        List<OrderProductDto> orderProductDtoList = orderProductService.getOrderProductList(orderId);
        log.info("order-product 조회");

        //product 조회
        List<UUID> productIds = orderProductDtoList.stream().map(OrderProductDto::getProductId).toList();
        Map<UUID, ProductDto> productMap = productClientPort.getProductAll(productIds);
        log.info("product 조회");

        //조합 후 반환
        return ResGetOrderByIdDto.of(order, address, orderProductDtoList, productMap, user);
    }

    @Transactional(readOnly = true)
    public Page<ResGetOrderListDto> getOrderList(UUID userId, Pageable pageable) {
        //주문 내역 조회
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);

        //각 주문의 OrderProduct 데이터 가져오기;
        Map<UUID, List<OrderProduct>> orderProductMap = orderProductService.getOrderProductsByOrderIds(
                orderPage.stream().toList());

        //각 주문의 Product 가져오기
        List<UUID> productIds = orderProductMap.values().stream()
                .flatMap(List::stream)
                .map(OrderProduct::getProductId)
                .toList();

        Map<UUID, ProductDto> productMap = productClientPort.getProductAll(productIds);
        // 4. ResGetOrderListDto 생성
        return orderPage.map(order -> ResGetOrderListDto.of(
                order,
                orderProductMap.get(order.getId()),
                productMap
        ));
    }

    public void updateOrderStatus(UUID orderId, ReqPutOrderDto reqPutOrderDto) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        order.updateStatus(reqPutOrderDto.getOrderStatus());
    }

    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        OrderStatus orderStatus = order.getStatus();
        if (!(orderStatus.equals(OrderStatus.CREATE) || orderStatus.equals(OrderStatus.PENDING))) {
            throw new ApplicationException(ErrorCode.ORDER_ALREADY_PROCESSED_EXCEPTION);
        }

        //비동기 처리
        //수량 복구
        //pending 일 경우 주문 취소 로직까지 필요
        order.updateStatus(OrderStatus.CANCEL);
    }

    public void refundOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        LocalDateTime orderCreatedAt = order.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(orderCreatedAt, now);

        OrderStatus orderStatus = order.getStatus();
        if (!orderStatus.equals(OrderStatus.COMPLETE) || duration.toHours() > 24) {
            throw new ApplicationException(ErrorCode.ORDER_NOT_CANCELLABLE_EXCEPTION);
        }

        //비동기 처리
        //수량 복구
        //주문 취소
        order.updateStatus(OrderStatus.REFUND);
    }

    private int calculateTotalAmount(List<Basket> basketList, Map<UUID, ProductDto> productDtoMap) {
        int totalAmount = 0;
        for (Basket basket : basketList) {

            ProductDto productDto = Optional.ofNullable(productDtoMap.get(basket.getProductId()))
                    .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

            if (productDto.getQuantity() < basket.getQuantity()) {
                throw new ApplicationException(ErrorCode.PRODUCT_INVALID_QUANTITY_EXCEPTION);
            }

            if (!productDto.getStatus().equals("ON_SALE")) {
                throw new ApplicationException(ErrorCode.PRODUCT_NOT_ON_SALE_EXCEPTION);
            }

            int productPrice =
                    (productDto.getDiscountPrice() == null) ? productDto.getPrice() : productDto.getDiscountPrice();
            totalAmount += (productPrice * basket.getQuantity());
        }
        return totalAmount;
    }

    private String getOrderName(List<ProductDto> productDtoList) {
        if (productDtoList.size() == 1) {
            return productDtoList.get(0).getName();
        }
        return productDtoList.get(0).getName() + "외 " + ((productDtoList.size() - 1) + "개");
    }

    private Map<UUID, ProductDto> convertListToMap(List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .collect(Collectors.toMap(ProductDto::getProductId, productDto -> productDto));
    }
}
