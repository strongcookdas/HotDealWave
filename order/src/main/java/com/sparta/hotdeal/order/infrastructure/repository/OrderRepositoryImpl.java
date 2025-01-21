package com.sparta.hotdeal.order.infrastructure.repository;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import com.sparta.hotdeal.order.infrastructure.repository.jpa.OrderRepositoryJpa;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderRepositoryJpa orderRepositoryJpa;

    @Override
    public Order save(Order order) {
        return orderRepositoryJpa.save(order);
    }

    @Override
    public Optional<Order> findByIdAndUserId(UUID orderId, UUID userId) {
        return orderRepositoryJpa.findByIdAndUserIdAndDeletedAtIsNull(orderId, userId);
    }

    @Override
    public Page<Order> findAllByUserId(UUID userId, Pageable pageable) {
        return orderRepositoryJpa.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
    }

    @Override
    public Optional<Order> findByIdAndDeletedAtIsNull(UUID orderId) {
        return orderRepositoryJpa.findByIdAndDeletedAtIsNull(orderId);
    }

    @Override
    public Order saveAndFlush(Order order) {
        return orderRepositoryJpa.saveAndFlush(order);
    }
}
