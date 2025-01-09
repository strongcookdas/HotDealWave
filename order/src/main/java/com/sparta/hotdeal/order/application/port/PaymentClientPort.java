package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.order.OrderDto;
import com.sparta.hotdeal.order.application.dtos.payment.PaymentRequestDto;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import java.util.List;

public interface PaymentClientPort {
    PaymentRequestDto readyPayment(OrderDto orderDto, List<Basket> basketList);
}
