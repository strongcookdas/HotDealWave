package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
    Order save(Order order);
}
