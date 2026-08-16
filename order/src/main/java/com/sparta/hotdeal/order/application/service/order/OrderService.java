package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPutOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
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
import com.sparta.hotdeal.order.event.service.OrderProducerService;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClientPort productClientPort;
    private final CouponClientPort couponClientPort;
    private final UserClientPort userClientPort;

    private final OrderProductService orderProductService;
    private final OrderBasketService orderBasketService;
    private final OrderCalculationService orderCalculationService;
    private final OrderProducerService orderProducerService;
    private final OrderPersistenceService orderPersistenceService;

    @Qualifier("orderParallelFetchExecutor")
    private final Executor orderParallelFetchExecutor;

    // 성능 테스트 단계별(Stage 0~5) 비교를 위한 토글. 기본값은 지금까지의 운영 동작(병렬 조회 + Kafka 비동기)과 동일.
    @Value("${perf-test.parallel-fetch:true}")
    private boolean parallelFetchEnabled;

    @Value("${perf-test.sync-reduce-quantity:false}")
    private boolean syncReduceQuantityEnabled;

    @PostConstruct
    public void logPerfTestConfig() {
        log.info("[PERF-TEST-CONFIG] parallelFetchEnabled={}, syncReduceQuantityEnabled={}",
                parallelFetchEnabled, syncReduceQuantityEnabled);
    }

    public ResPostOrderDto createOrder(UUID userId, String email, String role, ReqPostOrderDto req) {
        // 장바구니 목록 조회 (동기 처리)
        List<Basket> basketList = orderBasketService.getBasketList(userId, req.getBasketList());

        Map<UUID, ProductDto> productDtoMap;
        AddressDto addressDto;
        if (parallelFetchEnabled) {
            // 상품/주소 조회 병렬 처리
            CompletableFuture<Map<UUID, ProductDto>> productFuture = CompletableFuture.supplyAsync(
                    () -> getProductDetailsForBasketItems(basketList), orderParallelFetchExecutor
            );
            CompletableFuture<AddressDto> addressFuture = CompletableFuture.supplyAsync(
                    () -> userClientPort.getAddress(userId, email, role, req.getAddressId()), orderParallelFetchExecutor
            );
            productDtoMap = productFuture.join();
            addressDto = addressFuture.join();
        } else {
            // 상품/주소 순차 조회
            productDtoMap = getProductDetailsForBasketItems(basketList);
            addressDto = userClientPort.getAddress(userId, email, role, req.getAddressId());
        }

        // 쿠폰 검증 및 사용 (동기 처리, 외부 API 호출)
        CouponValidationDto couponValidationDto = validateAndUseCoupon(req.getCouponId(), basketList, productDtoMap);

        // 총 금액 계산
        int totalAmount = orderCalculationService.calculateTotalAmount(basketList, productDtoMap);

        // 여기까지는 전부 외부 API 호출/순수 계산이라 DB 커넥션을 붙잡지 않는다.
        // 실제 DB 쓰기(주문 저장 + 주문상품 저장 + 장바구니 삭제)만 짧은 트랜잭션으로 묶는다.
        Order order = orderPersistenceService.persistOrder(
                addressDto.getAddressId(),
                userId,
                totalAmount,
                getOrderName(productDtoMap),
                req.getCouponId(),
                couponValidationDto.getTotalDiscountAmount(),
                basketList,
                productDtoMap
        );

        // 재고 차감: 동기 Feign 호출 vs Kafka 비동기 메시지 (트랜잭션 커밋 이후, DB 커넥션과 무관)
        if (syncReduceQuantityEnabled) {
            productClientPort.reduceProductQuantitySync(order, basketList);
        } else {
            orderProducerService.sendReduceProductQuantityMessage(order, basketList);
        }

        return ResPostOrderDto.of(order.getId());
    }


    private Map<UUID, ProductDto> getProductDetailsForBasketItems(List<Basket> basketList) {
        List<UUID> productIds = basketList.stream().map(Basket::getProductId).toList();
        Map<UUID, ProductDto> productDtoMap = productClientPort.getProductAll(productIds);
        log.info("상품 목록 조회 API 호출");
        return productDtoMap;
    }

    private CouponValidationDto validateAndUseCoupon(UUID couponId, List<Basket> basketList,
                                                     Map<UUID, ProductDto> productDtoMap) {

        CouponValidationDto couponValidationDto = couponClientPort.validateCoupon(couponId, basketList, productDtoMap);
        log.info("쿠폰 유효성 체크 API 호출");

        if (couponValidationDto.isValid()) {
            couponClientPort.useCoupon(couponId);
            log.info("쿠폰 사용 API 호출");
        }

        return couponValidationDto;
    }

    private String getOrderName(Map<UUID, ProductDto> productDtoMap) {
        List<ProductDto> productDtoList = productDtoMap.values().stream().toList();

        if (productDtoList.size() == 1) {
            return productDtoList.get(0).getName();
        }
        return productDtoList.get(0).getName() + " 외 " + (productDtoList.size() - 1) + "개";
    }

    @Transactional(readOnly = true)
    public ResGetOrderByIdDto getOrderDetail(UUID userId, String email, String role, UUID orderId) {

        Order order = getOrderByUserRole(role, userId, orderId);

        //주소 조회 - (현재 자신 저장한 주소만 볼 수 있도록 권한 설정이 되어 있어 주석)
        //AddressDto address = userClientPort.getAddress(userId, email, role, order.getAddressId());
        //log.info("주소 조회");

        UserDto user = userClientPort.getUserById(userId, email, role);
        log.info("유저 조회 API 호출");

        List<OrderProduct> orderProductList = orderProductService.getOrderProductList(orderId);

        Map<UUID, ProductDto> productMap = getProductDetailsForOrderProduct(orderProductList);

        return ResGetOrderByIdDto.of(order, orderProductList, productMap, user);
    }

    private Order getOrderByUserRole(String role, UUID userId, UUID orderId) {
        if (role.equals("ROLE_MASTER")) {
            return orderRepository.findByIdAndDeletedAtIsNull(orderId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));
        }

        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));
    }

    private Map<UUID, ProductDto> getProductDetailsForOrderProduct(List<OrderProduct> orderProductList) {
        List<UUID> productIds = orderProductList.stream().map(OrderProduct::getProductId).toList();
        Map<UUID, ProductDto> productDtoMap = productClientPort.getProductAll(productIds);
        log.info("상품 목록 조회 API 호출");
        return productDtoMap;
    }

    @Transactional(readOnly = true)
    public Page<ResGetOrderListDto> getOrderList(UUID userId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);

        Map<UUID, List<OrderProduct>> orderProductMap = getOrderProductsGroupedByOrderId(orderPage);

        Map<UUID, ProductDto> productDtoMap = getProductMapByOrderProducts(orderProductMap);
        log.info("상품 목록 조회 API 호출");

        return convertOrderPageDto(orderPage, orderProductMap, productDtoMap);
    }

    private Map<UUID, List<OrderProduct>> getOrderProductsGroupedByOrderId(Page<Order> orderPage) {
        List<Order> orderList = orderPage.stream().toList();
        return orderProductService.getOrderProductsByOrderIds(orderList);
    }

    private Map<UUID, ProductDto> getProductMapByOrderProducts(Map<UUID, List<OrderProduct>> orderProductMap) {
        List<UUID> productIds = orderProductMap.values().stream()
                .flatMap(List::stream)
                .map(OrderProduct::getProductId)
                .toList();

        return productClientPort.getProductAll(productIds);
    }

    private Page<ResGetOrderListDto> convertOrderPageDto(Page<Order> orderPage,
                                                         Map<UUID, List<OrderProduct>> orderProductMap,
                                                         Map<UUID, ProductDto> productDtoMap) {
        return orderPage.map(order -> ResGetOrderListDto.of(
                order,
                orderProductMap.get(order.getId()),
                productDtoMap
        ));
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, ReqPutOrderDto reqPutOrderDto) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        order.updateStatus(reqPutOrderDto.getOrderStatus());
    }

    @Transactional
    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = findByIdAndUserId(orderId, userId);
        checkOrderStatusCancellable(order);

        order.updateStatus(OrderStatus.CANCEL);
        restoreProductQuantityByOrderCancel(order);

        orderProducerService.sendCancelPaymentMessage(order);
    }

    private void checkOrderStatusCancellable(Order order) {
        OrderStatus orderStatus = order.getStatus();
        if (!(orderStatus.equals(OrderStatus.CREATE) || orderStatus.equals(OrderStatus.PENDING))) {
            throw new ApplicationException(ErrorCode.ORDER_ALREADY_PROCESSED_EXCEPTION);
        }
    }

    private void restoreProductQuantityByOrderCancel(Order order) {
        List<OrderProduct> orderProductList = orderProductService.getOrderProductList(order.getId());
        productClientPort.restoreProductList(order, orderProductList);
        log.info("상품 수량 복구 API 호출");
    }

    @Transactional
    public void refundOrder(UUID userId, UUID orderId) {
        Order order = findByIdAndUserId(orderId, userId);
        checkOrderRefundableDate(order);
        order.updateStatus(OrderStatus.REFUND);
        orderProducerService.sendRefundPaymentMessage(order);
    }


    private void checkOrderRefundableDate(Order order) {
        LocalDateTime orderCreatedAt = order.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(orderCreatedAt, now);

        OrderStatus orderStatus = order.getStatus();
        if (!orderStatus.equals(OrderStatus.COMPLETE) || duration.toHours() > 24) {
            throw new ApplicationException(ErrorCode.ORDER_NOT_CANCELLABLE_EXCEPTION);
        }
    }

    private Order findByIdAndUserId(UUID orderId, UUID userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));
    }

}
