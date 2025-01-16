package com.sparta.hotdeal.order.infrastructure.repository;

import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.repository.OrderProductRepository;
import com.sparta.hotdeal.order.infrastructure.repository.jpa.OrderProductRepositoryJpa;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepository {

    private final OrderProductRepositoryJpa orderProductRepositoryJpa;

    @Override
    public List<OrderProduct> saveAllOrderProduct(List<OrderProduct> orderProductList) {
        return orderProductRepositoryJpa.saveAll(orderProductList);
    }

    @Override
    public List<OrderProduct> findAllByOrderId(UUID orderId) {
        return orderProductRepositoryJpa.findAllByOrderIdAndDeletedAtIsNull(orderId);
    }
}
