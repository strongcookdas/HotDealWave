package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasketRepository {
    Basket save(Basket basket);

    List<Basket> findByIdInAndUserId(List<UUID> ids, UUID userId);

    Optional<Basket> findByIdAndUserId(UUID basketId, UUID userId);

    Page<Basket> findAllByUserId(UUID userId, Pageable pageable);
}
