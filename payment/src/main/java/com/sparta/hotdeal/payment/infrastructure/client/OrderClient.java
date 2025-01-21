package com.sparta.hotdeal.payment.infrastructure.client;

import com.sparta.hotdeal.payment.application.dtos.ResponseDto;
import com.sparta.hotdeal.payment.infrastructure.config.OrderClientConfig;
import com.sparta.hotdeal.payment.infrastructure.dto.order.req.ReqPutOrderDto;
import com.sparta.hotdeal.payment.infrastructure.dto.order.res.ResGetOrderByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", configuration = OrderClientConfig.class)
public interface OrderClient {
    @GetMapping(value = "/api/v1/orders/{orderId}")
    ResponseDto<ResGetOrderByIdDto> getOrderById(@PathVariable UUID orderId);
    @PutMapping(value = "/api/v1/orders/{orderId}/status")
    ResponseDto<Void> updateOrderStatus(@PathVariable UUID orderId, @RequestBody ReqPutOrderDto reqPostPaymentDto);
}
