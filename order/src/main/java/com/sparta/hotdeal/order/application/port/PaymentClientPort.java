package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.order.OrderDto;
import com.sparta.hotdeal.order.application.dtos.payment.PaymentRequestDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;
import java.util.UUID;

public interface PaymentClientPort {
    PaymentRequestDto readyPayment(UUID userId, String email, String role, OrderDto orderDto, List<Basket> basketList);
}
