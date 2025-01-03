package com.sparta.hotdeal.order.application.service.client;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import java.util.UUID;

public interface ProductClientService {
    ProductDto getProduct(UUID productId);
}
