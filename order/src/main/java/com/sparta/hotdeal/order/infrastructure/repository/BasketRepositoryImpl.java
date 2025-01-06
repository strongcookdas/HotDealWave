package com.sparta.hotdeal.order.infrastructure.repository;

import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.repository.BasketRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepositoryImpl extends JpaRepository<Basket, UUID>, BasketRepository {
}
