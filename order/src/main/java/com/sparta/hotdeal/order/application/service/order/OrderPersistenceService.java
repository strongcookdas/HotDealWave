package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 생성의 DB 쓰기만 담당한다. Feign 호출 등 외부 API 호출은 이 클래스에 들어오기 전에
 * 끝나 있어야 하며, 트랜잭션은 실제 DB 작업 구간만 짧게 묶는다.
 */
@Service
@RequiredArgsConstructor
public class OrderPersistenceService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;
    private final OrderBasketService orderBasketService;

    @Transactional
    public Order persistOrder(
            UUID addressId,
            UUID userId,
            int totalAmount,
            String orderName,
            UUID couponId,
            int discountAmount,
            List<Basket> basketList,
            Map<UUID, ProductDto> productDtoMap
    ) {
        Order order = Order.create(addressId, userId, totalAmount, orderName, couponId, discountAmount);
        orderRepository.save(order);

        orderProductService.saveOrderProductList(order, basketList, productDtoMap);
        orderBasketService.deleteBasketList(basketList);

        return order;
    }
}