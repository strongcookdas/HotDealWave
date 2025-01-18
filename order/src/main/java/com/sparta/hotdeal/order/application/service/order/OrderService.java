package com.sparta.hotdeal.order.application.service.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.coupon.CouponValidationDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.req.ReqPutOrderDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderByIdDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResGetOrderListDto;
import com.sparta.hotdeal.order.application.dtos.order.res.ResPostOrderDto;
import com.sparta.hotdeal.order.application.dtos.payment.req.ReqPaymentCancelMessage;
import com.sparta.hotdeal.order.application.dtos.payment.req.ReqPaymentRefundMessage;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
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
import com.sparta.hotdeal.order.event.producer.OrderEventProducer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderService {

    @Value("${spring.kafka.topics.request-product}")
    private String reduceProductQuantityTopic;
    @Value("${spring.kafka.topics.cancel-payment}")
    private String cancelPaymentTopic;
    @Value("${spring.kafka.topics.refund-payment}")
    private String refundPaymentTopic;

    private final OrderRepository orderRepository;
    private final ProductClientPort productClientPort;
    private final CouponClientPort couponClientPort;
    private final UserClientPort userClientPort;

    private final OrderProductService orderProductService;
    private final OrderBasketService orderBasketService;
    private final OrderCalculationService orderCalculationService;

    private final OrderEventProducer orderEventProducer;
    private final ObjectMapper objectMapper;


    public ResPostOrderDto createOrder(UUID userId, String email, String role, ReqPostOrderDto req) {
        List<Basket> basketList = orderBasketService.getBasketList(userId, req.getBasketList());

        Map<UUID, ProductDto> productDtoMap = getProductDetailsForBasketItems(basketList);

        CouponValidationDto couponValidationDto = validateAndUseCoupon(req.getCouponId(), basketList,
                productDtoMap);

        AddressDto addressDto = userClientPort.getAddress(userId, email, role, req.getAddressId());
        log.info("주소 단건 조회 API 호출");

        int totalAmount = orderCalculationService.calculateTotalAmount(basketList, productDtoMap);

        Order order = saveOrder(
                addressDto.getAddressId(),
                userId,
                totalAmount,
                getOrderName(productDtoMap),
                req.getCouponId(),
                couponValidationDto.getTotalDiscountAmount()
        );

        orderProductService.saveOrderProductList(order, basketList, productDtoMap);
        orderBasketService.deleteBasketList(basketList);

        sendReduceProductQuantityMessage(order, basketList);

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

    private Order saveOrder(
            UUID addressId,
            UUID userId,
            int totalAmount,
            String orderName,
            UUID couponId,
            int discountAmount
    ) {
        Order order = Order.create(
                addressId,
                userId,
                totalAmount,
                orderName,
                couponId,
                discountAmount
        );

        return orderRepository.save(order);
    }

    private String getOrderName(Map<UUID, ProductDto> productDtoMap) {
        List<ProductDto> productDtoList = productDtoMap.values().stream().toList();

        if (productDtoList.size() == 1) {
            return productDtoList.get(0).getName();
        }
        return productDtoList.get(0).getName() + " 외 " + (productDtoList.size() - 1) + "개";
    }

    private void sendReduceProductQuantityMessage(Order order, List<Basket> basketList) {
        try {
            ReqProductReduceQuantityDto req = ReqProductReduceQuantityDto.create(order.getId(), basketList);
            String message = objectMapper.writeValueAsString(req);
            orderEventProducer.sendMessage(reduceProductQuantityTopic, req.getOrderId().toString(), message);
        } catch (Exception e) {
            log.error("상품 재고 차감 메세지 전송 실패 : {}", e.getMessage());
            this.cancelOrderByIdForMessage(order.getId());
        }
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

    public void updateOrderStatus(UUID orderId, ReqPutOrderDto reqPutOrderDto) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        order.updateStatus(reqPutOrderDto.getOrderStatus());
    }

    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = findByIdAndUserId(orderId, userId);
        checkOrderStatusCancellable(order);

        order.updateStatus(OrderStatus.CANCEL);
        restoreProductQuantityByOrderCancel(order);

        sendCancelPaymentMessage(order);
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

    public void cancelOrderByIdForMessage(UUID orderId) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        order.updateStatus(OrderStatus.CANCEL);
    }


    public void refundOrder(UUID userId, UUID orderId) {
        Order order = findByIdAndUserId(orderId, userId);
        checkOrderRefundableDate(order);

        order.updateStatus(OrderStatus.REFUND);

        sendRefundPaymentMessage(order);

    }

    private void checkOrderRefundableDate(Order order){
        LocalDateTime orderCreatedAt = order.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(orderCreatedAt, now);

        OrderStatus orderStatus = order.getStatus();
        if (!orderStatus.equals(OrderStatus.COMPLETE) || duration.toHours() > 24) {
            throw new ApplicationException(ErrorCode.ORDER_NOT_CANCELLABLE_EXCEPTION);
        }
    }

    private void sendCancelPaymentMessage(Order order) {
        try {
            ReqPaymentCancelMessage reqPaymentCancelMessage = ReqPaymentCancelMessage.of(order);
            String message = objectMapper.writeValueAsString(reqPaymentCancelMessage);
            orderEventProducer.sendMessage(cancelPaymentTopic, order.getId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    private void sendRefundPaymentMessage(Order order) {
        try {
            ReqPaymentRefundMessage reqPaymentRefundMessage = ReqPaymentRefundMessage.of(order);
            String message = objectMapper.writeValueAsString(reqPaymentRefundMessage);
            orderEventProducer.sendMessage(refundPaymentTopic, order.getId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    private Order findByIdAndUserId(UUID orderId, UUID userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND_EXCEPTION));
    }

}
