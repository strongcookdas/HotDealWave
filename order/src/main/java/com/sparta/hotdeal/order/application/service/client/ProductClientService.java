package com.sparta.hotdeal.order.application.service.client;

import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductByIdForBasketDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForBasketDto;
import java.util.List;
import java.util.UUID;

public interface ProductClientService {
    ResGetProductByIdForBasketDto getProduct(UUID productId);

    List<ResGetProductListForBasketDto> getProductList(List<UUID> productIds);
}
