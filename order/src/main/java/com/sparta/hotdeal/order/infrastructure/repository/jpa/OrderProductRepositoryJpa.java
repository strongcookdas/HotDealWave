package com.sparta.hotdeal.order.infrastructure.repository.jpa;

import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderProductRepositoryJpa extends JpaRepository<OrderProduct, UUID> {
    List<OrderProduct> findAllByOrderIdAndDeletedAtIsNull(UUID orderId);
}
