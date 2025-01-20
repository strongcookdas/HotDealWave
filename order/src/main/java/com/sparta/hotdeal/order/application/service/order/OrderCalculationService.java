package com.sparta.hotdeal.order.application.service.order;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderCalculationService {
    public int calculateTotalAmount(List<Basket> basketList, Map<UUID, ProductDto> productDtoMap) {
        int totalAmount = 0;

        for (Basket basket : basketList) {
            ProductDto productDto = validateAndGetBasketProductFromMap(basket, productDtoMap);
            int productPrice = getProductPrice(productDto);
            totalAmount += calculateBasketItemAmount(basket, productPrice);
        }

        log.info("총 금액 계산: {}", totalAmount);
        return totalAmount;
    }

    private ProductDto validateAndGetBasketProductFromMap(Basket basket, Map<UUID, ProductDto> productDtoMap) {
        ProductDto productDto = Optional.ofNullable(productDtoMap.get(basket.getProductId()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION));

        if (productDto.getQuantity() < basket.getQuantity()) {
            throw new ApplicationException(ErrorCode.PRODUCT_INVALID_QUANTITY_EXCEPTION);
        }

        if (!"ON_SALE".equals(productDto.getStatus())) {
            throw new ApplicationException(ErrorCode.PRODUCT_NOT_ON_SALE_EXCEPTION);
        }

        return productDto;
    }

    private int getProductPrice(ProductDto productDto) {
        if (productDto.getDiscountPrice() == null) {
            return productDto.getPrice();
        }
        return productDto.getDiscountPrice();
    }

    private int calculateBasketItemAmount(Basket basket, int productPrice) {
        return productPrice * basket.getQuantity();
    }
}
