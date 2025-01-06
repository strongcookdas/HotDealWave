package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.repository.OrderProductRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j(topic = "[Order-Product]")
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public void createOrderProductList(Order order, List<Basket> basketList,
                                       Map<UUID, ProductDto> productMap) {

        List<OrderProduct> orderProductList = basketList.stream()
                .map(basket -> {
                    ProductDto product = getProductOrThrow(productMap, basket.getProductId());
                    return OrderProduct.create(
                            order.getId(),
                            product.getProductId(),
                            basket.getQuantity(),
                            (product.getDiscountPrice() == null) ? product.getPrice() : product.getDiscountPrice()
                    );
                }).toList();

        orderProductRepository.saveAllOrderProduct(orderProductList);

    }

    public List<OrderProductDto> getOrderProductList(UUID orderId) {
        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(orderId);
        return orderProductList.stream().map(OrderProductDto::of).toList();
    }

    public Map<UUID, List<OrderProductDto>> getOrderProductsByOrderIds(List<UUID> orderIds) {
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderIdIn(orderIds);

        return orderProducts.stream()
                .map(OrderProductDto::of)
                .collect(Collectors.groupingBy(OrderProductDto::getOrderId));
    }

    private ProductDto getProductOrThrow(
            Map<UUID, ProductDto> productMap,
            UUID productId) {
        return Optional.ofNullable(productMap.get(productId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));
    }
}
