package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.product.ProductByIdtDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderProduct;
import com.sparta.hotdeal.order.infrastructure.client.ProductClient;
import com.sparta.hotdeal.order.infrastructure.dtos.product.req.ReqPutProductQuantityDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.res.ResGetProductListDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClientAdapter implements ProductClientPort {

    private final ProductClient productClient;

    @Override
    @Cacheable(cacheNames = "productListCache", key = "T(java.util.Objects).hash(#productIds.stream().sorted().toList())")
    public Map<UUID, ProductDto> getProductAll(List<UUID> productIds) {
        List<ResGetProductListDto> resGetProductListDtoList = productClient.getProductList(productIds).getData()
                .toList();
        return resGetProductListDtoList.stream()
                .map(ResGetProductListDto::toGetProductListForOrderDto)
                .collect(Collectors.toMap(
                        ProductDto::getProductId,
                        product -> product
                ));
    }

    @Override
    @Cacheable(cacheNames = "productCache", key = "args[0]")
    public ProductByIdtDto getProduct(UUID productId) {
        return productClient.getProduct(productId).getData().toDto();
    }

    @Override
    public List<ProductDto> getProductALL(List<UUID> productIds) {
        List<ResGetProductListDto> resGetProductListDtoList = productClient.getProductList(productIds).getData()
                .stream().toList();

        return resGetProductListDtoList.stream().map(ResGetProductListDto::toGetProductListForOrderDto).toList();
    }

    @Override
    public void restoreProductList(Order order, List<OrderProduct> orderProductDtoList) {
        ReqPutProductQuantityDto reqPutProductQuantityDto = ReqPutProductQuantityDto.of(order, orderProductDtoList);
        productClient.reduceQuantity(reqPutProductQuantityDto);
    }
}
