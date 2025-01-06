package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductByIdDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.ResGetProductListDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping(value = "/api/v1/products/{productId}")
    ResponseDto<ResGetProductByIdDto> getProductFromAPI(@PathVariable UUID productId);

    @GetMapping(value = "/api/v1/products")
    ResponseDto<Page<ResGetProductListDto>> getProductListFromAPI(@RequestParam("productIds") List<UUID> productIds);
}
