package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.config.ProductClientConfig;
import com.sparta.hotdeal.order.infrastructure.dtos.product.req.ReqPatchProductQuantityDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.res.ResGetProductByIdDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.res.ResGetProductListDto;
import com.sparta.hotdeal.order.infrastructure.dtos.product.res.ResPatchReduceProductQuantityDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", configuration = ProductClientConfig.class)
public interface ProductClient {
    @GetMapping(value = "/api/v1/products/{productId}")
    ResponseDto<ResGetProductByIdDto> getProduct(@PathVariable UUID productId);

    @GetMapping(value = "/api/v1/products")
    ResponseDto<Page<ResGetProductListDto>> getProductList(@RequestParam("productIds") List<UUID> productIds);

    @PutMapping("/api/v1/products/reduce-quantity")
    ResponseDto<List<ResPatchReduceProductQuantityDto>> reduceQuantity(
            @RequestBody List<ReqPatchProductQuantityDto> reqPatchProductQuantityDto);
}
