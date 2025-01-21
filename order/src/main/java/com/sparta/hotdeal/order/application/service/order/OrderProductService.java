package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.domain.repository.OrderProductRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public void saveOrderProductList(Order order, List<Basket> basketList, Map<UUID, ProductDto> productDtoMap) {
        List<OrderProduct> orderProductList = basketList.stream()
                .map(basket -> {
                    ProductDto productDto = getProductOrThrow(productDtoMap, basket.getProductId());
                    return OrderProduct.create(
                            order,
                            productDto.getProductId(),
                            basket.getQuantity(),
                            getProductPrice(productDto)
                    );
                }).toList();

        orderProductRepository.saveAllOrderProduct(orderProductList);
    }

    private int getProductPrice(ProductDto productDto) {
        if (productDto.getDiscountPrice() == null) {
            return productDto.getPrice();
        }
        return productDto.getDiscountPrice();
    }

    public List<OrderProduct> getOrderProductList(UUID orderId) {
        return orderProductRepository.findAllByOrderId(orderId);
    }

    public Map<UUID, List<OrderProduct>> getOrderProductsByOrderIds(List<Order> orderList) {
        Map<UUID, List<OrderProduct>> orderProductMap = new HashMap<>();
        for (Order order : orderList) {
            List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(order.getId());
            orderProductMap.put(order.getId(), orderProductList);
        }

        return orderProductMap;
    }

    private ProductDto getProductOrThrow(Map<UUID, ProductDto> productMap, UUID productId) {
        return Optional.ofNullable(productMap.get(productId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));
    }
}
