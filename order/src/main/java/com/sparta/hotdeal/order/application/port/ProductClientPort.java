package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.product.ProductByIdtDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductClientPort {

    Map<UUID, ProductDto> getProductAll(List<UUID> productIds);

    ProductByIdtDto getProduct(UUID productId);

    List<ProductDto> getProductALL(List<UUID> productIds);

    void restoreProductList(Order order, List<OrderProduct> orderProductDtoList);
}
