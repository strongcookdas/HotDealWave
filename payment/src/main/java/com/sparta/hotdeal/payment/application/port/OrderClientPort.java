package com.sparta.hotdeal.payment.application.port;

import java.util.UUID;

public interface OrderClientPort {
    void updateOrderStatus(UUID orderId, String status);
}
