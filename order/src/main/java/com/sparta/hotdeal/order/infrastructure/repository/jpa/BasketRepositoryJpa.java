package com.sparta.hotdeal.order.infrastructure.repository.jpa;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BasketRepositoryJpa extends JpaRepository<Basket, UUID> {
    List<Basket> findAllByIdInAndUserIdAndDeletedAtIsNull(List<UUID> ids, UUID userId);

    Optional<Basket> findByIdAndUserIdAndDeletedAtIsNull(UUID basketId, UUID userId);

    Page<Basket> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);
}
