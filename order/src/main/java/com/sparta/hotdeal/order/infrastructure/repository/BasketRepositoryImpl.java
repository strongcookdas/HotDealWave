package com.sparta.hotdeal.order.infrastructure.repository;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import com.sparta.hotdeal.order.infrastructure.repository.jpa.BasketRepositoryJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BasketRepositoryImpl implements BasketRepository {
    private final BasketRepositoryJpa basketRepositoryJpa;

    @Override
    public Basket save(Basket basket) {
        return basketRepositoryJpa.save(basket);
    }

    @Override
    public List<Basket> findByIdInAndUserId(List<UUID> ids, UUID userId) {
        return basketRepositoryJpa.findAllByIdInAndUserIdAndDeletedAtIsNull(ids, userId);
    }

    @Override
    public Optional<Basket> findByIdAndUserId(UUID basketId, UUID userId) {
        return basketRepositoryJpa.findByIdAndUserIdAndDeletedAtIsNull(basketId, userId);
    }

    @Override
    public void delete(Basket basket) {

    }

    @Override
    public Page<Basket> findAllByUserId(UUID userId, Pageable pageable) {
        return basketRepositoryJpa.findAllByUserId(userId, pageable);
    }
}
