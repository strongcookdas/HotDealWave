package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import com.sparta.hotdeal.order.application.service.client.ProductClientService;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient extends ProductClientService {
    @GetMapping(value = "/api/v1/products/{productId}")
    ResponseDto<ResGetProductByIdDto> getProductFromAPI(@PathVariable UUID productId);

    @Override
    default ProductDto getProduct(UUID productId) {
        //return this.getProductFromAPI(productId).getData().toDto();
        return ResGetProductByIdDto.createDummy().toDto();
    }
}
