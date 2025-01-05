package com.sparta.hotdeal.order.application.service.client;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import java.util.List;
import java.util.UUID;

public interface ProductClientService {
    ProductDto getProduct(UUID productId);

    List<ProductListDto> getProductList(List<UUID> productIds);

    List<ResGetProductListForOrderDto> getProductListForOrder(List<UUID> productIds);

    void reduceProductQuantity(List<ReqProductReduceQuantityDto> reqProductReduceQuantityDtoList);
}
