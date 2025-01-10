package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findByIdAndUserId(UUID orderId, UUID userId);

    Page<Order> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Order> findByIdAndDeletedAtIsNull(UUID orderId);
}
