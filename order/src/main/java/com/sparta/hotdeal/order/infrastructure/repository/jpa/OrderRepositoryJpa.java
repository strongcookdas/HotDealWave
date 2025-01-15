package com.sparta.hotdeal.order.infrastructure.repository.jpa;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderRepositoryJpa extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndUserIdAndDeletedAtIsNull(UUID orderId, UUID userId);

    Page<Order> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    Optional<Order> findByIdAndDeletedAtIsNull(UUID orderId);
}
