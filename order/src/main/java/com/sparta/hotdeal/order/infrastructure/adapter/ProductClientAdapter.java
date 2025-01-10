package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.product.ProductByIdtDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.port.ProductClientPort;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.infrastructure.client.ProductClient;
import com.sparta.hotdeal.order.infrastructure.dtos.product.req.ReqPatchProductQuantityDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.res.ResGetProductListDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClientAdapter implements ProductClientPort {

    private final ProductClient productClient;

    @Override
    public Map<UUID, ProductDto> getProductAll(List<UUID> productIds) {
        List<ResGetProductListDto> resGetProductListDtoList = productClient.getProductList(productIds).getData()
                .toList();
//        List<ResGetProductListDto> resGetProductListDtoList = ResGetProductListDto.createDummyDataList();
        return resGetProductListDtoList.stream()
                .map(ResGetProductListDto::toGetProductListForOrderDto)
                .collect(Collectors.toMap(
                        ProductDto::getProductId,
                        product -> product
                ));
    }

    @Override
    public void reduceProductQuantity(List<Basket> basketList) {
        // API 호출 구현 예정
        List<ReqPatchProductQuantityDto> reqProductReduceQuantityDtoList = basketList.stream()
                .map(basket -> ReqPatchProductQuantityDto.create(basket.getProductId(), basket.getQuantity()))
                .toList();
        productClient.reduceQuantity(reqProductReduceQuantityDtoList);
    }

    @Override
    public ProductByIdtDto getProduct(UUID productId) {
        return productClient.getProduct(productId).getData().toDto();
//        return ResGetProductByIdDto.createDummyData(productId).toDto();
    }

    @Override
    public List<ProductDto> getProductALL(List<UUID> productIds) {
        List<ResGetProductListDto> resGetProductListDtoList = productClient.getProductList(productIds).getData()
                .stream().toList();

        return resGetProductListDtoList.stream().map(ResGetProductListDto::toGetProductListForOrderDto).toList();
    }
}
