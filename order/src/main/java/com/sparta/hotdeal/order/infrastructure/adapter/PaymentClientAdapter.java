package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.order.OrderDto;
import com.sparta.hotdeal.order.application.dtos.payment.PaymentRequestDto;
import com.sparta.hotdeal.order.application.port.PaymentClientPort;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.infrastructure.client.PaymentClient;
import com.sparta.hotdeal.order.infrastructure.dtos.payment.req.ReqPostPaymentDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentClientAdapter implements PaymentClientPort {
    private final PaymentClient paymentClient;

    @Override
    public PaymentRequestDto readyPayment(OrderDto orderDto, List<Basket> basketList) {
        //Basket도 중간 DTO 사용이 필요 추후 구현
        int quantity = basketList.stream().mapToInt(Basket::getQuantity).sum();
        ReqPostPaymentDto reqPostPaymentDto = ReqPostPaymentDto.create(
                orderDto.getId(),
                orderDto.getName(),
                quantity,
                orderDto.getTotalAmount()
        );

        return paymentClient.readyPayment(reqPostPaymentDto).getData().toPaymentRequestDto();
    }
}
