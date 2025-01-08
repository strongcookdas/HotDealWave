package com.sparta.hotdeal.product.infrastructure.client;

import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.service.client.OrderClientService;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetOrderByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient extends OrderClientService {

    @GetMapping("/api/v1/orders/{orderId}")
    ResponseDto<ResGetOrderByIdDto> getOrderById(@PathVariable("orderId") UUID orderId);

    @Override
    default ResGetOrderByIdDto getOrder(UUID orderId) {
        return ResGetOrderByIdDto.createDummy();
    }
}