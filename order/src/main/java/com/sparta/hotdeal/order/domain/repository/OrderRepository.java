package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByIdAndUserId(UUID orderId, UUID userId);
}
