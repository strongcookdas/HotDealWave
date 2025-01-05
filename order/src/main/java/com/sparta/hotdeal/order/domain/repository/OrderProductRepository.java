package com.sparta.hotdeal.order.domain.repository;

import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository {
    List<OrderProduct> saveAllOrderProduct(List<OrderProduct> orderProductList);
}
