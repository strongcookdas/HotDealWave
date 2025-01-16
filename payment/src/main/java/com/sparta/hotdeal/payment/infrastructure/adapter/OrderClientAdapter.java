package com.sparta.hotdeal.payment.infrastructure.adapter;

import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.port.OrderClientPort;
import com.sparta.hotdeal.payment.infrastructure.client.OrderClient;
import com.sparta.hotdeal.payment.infrastructure.dto.order.req.ReqPutOrderDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderClientAdapter implements OrderClientPort {
    private final OrderClient orderClient;

    @Override
    public void updateOrderStatus(UUID orderId, String status) {
        ReqPutOrderDto reqPutOrderDto = new ReqPutOrderDto(status);
        orderClient.updateOrderStatus(orderId, reqPutOrderDto);
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        return orderClient.getOrderById(orderId).getData().toOrderDto();
    }
}
