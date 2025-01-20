package com.sparta.hotdeal.payment.application.port;

import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import java.util.UUID;

public interface OrderClientPort {
    void updateOrderStatus(UUID orderId, String status);

    OrderDto getOrderById(UUID orderId);
}
