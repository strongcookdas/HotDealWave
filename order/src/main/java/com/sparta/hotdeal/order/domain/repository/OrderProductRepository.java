package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import java.util.List;
import java.util.UUID;

public interface OrderProductRepository {
    List<OrderProduct> saveAllOrderProduct(List<OrderProduct> orderProductList);
    List<OrderProduct> findAllByOrderId(UUID orderId);
}
