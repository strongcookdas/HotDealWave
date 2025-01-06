package com.sparta.hotdeal.order.infrastructure.repository;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryImpl extends JpaRepository<Order, UUID>, OrderRepository {
    Optional<Order> findByIdAndUserIdAndDeletedAtIsNull(UUID orderId, UUID userId);
    @Override
    default Optional<Order> findByIdAndUserId(UUID orderId, UUID userId){
        return findByIdAndUserIdAndDeletedAtIsNull(orderId, userId);
    }
}
