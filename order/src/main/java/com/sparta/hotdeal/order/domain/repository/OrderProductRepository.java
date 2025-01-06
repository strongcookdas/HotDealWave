package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository {
    List<OrderProduct> saveAllOrderProduct(List<OrderProduct> orderProductList);
    List<OrderProduct> findAllByOrderId(UUID orderId);
}
