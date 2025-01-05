package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import com.sparta.hotdeal.order.application.dtos.product.req.ReqProductReduceQuantityDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductByIdDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductListDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient extends ProductClientService {
    @GetMapping(value = "/api/v1/products/{productId}")
    ResponseDto<ResGetProductByIdDto> getProductFromAPI(@PathVariable UUID productId);

    @GetMapping(value = "/api/v1/products")
    ResponseDto<Page<ResGetProductListDto>> getProductListFromAPI(@RequestParam("productIds") List<UUID> productIds);

    @Override
    default ProductDto getProduct(UUID productId) {
        return this.getProductFromAPI(productId).getData().toDto();
    }

    @Override
    default List<ProductListDto> getProductList(List<UUID> productIds) {
        List<ResGetProductListDto> list = this.getProductListFromAPI(productIds).getData().toList();
        return list.stream()
                .map(ResGetProductListDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    default List<ResGetProductListForOrderDto> getProductListForOrder(List<UUID> productIds) {
        List<ResGetProductListDto> list = this.getProductListFromAPI(productIds).getData().toList();
        return list.stream()
                .map(ResGetProductListDto::toGetProductListForOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    default void reduceProductQuantity(List<ReqProductReduceQuantityDto> reqProductReduceQuantityDtoList){
        // api 호출
    }
}
