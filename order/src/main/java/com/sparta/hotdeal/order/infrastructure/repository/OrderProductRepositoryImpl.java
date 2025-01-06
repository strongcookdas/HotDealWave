package com.sparta.hotdeal.order.infrastructure.repository;

import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.repository.OrderProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepositoryImpl extends JpaRepository<OrderProduct, UUID>, OrderProductRepository {
    @Override
    default List<OrderProduct> saveAllOrderProduct(List<OrderProduct> orderProductList){
        return saveAllAndFlush(orderProductList);
    }
}
