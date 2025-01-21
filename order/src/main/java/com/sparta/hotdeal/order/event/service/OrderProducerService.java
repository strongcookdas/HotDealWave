package com.sparta.hotdeal.order.event.service;

import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import com.sparta.hotdeal.order.domain.entity.basket.Basket;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.order.domain.repository.OrderRepository;
import com.sparta.hotdeal.order.event.producer.OrderEventProducer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducerService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public void sendReduceProductQuantityMessage(Order order, List<Basket> basketList) {
        try {
            orderEventProducer.sendReduceProductQuantityMessage(order, basketList);
        } catch (Exception e) {
            log.error("상품 수량 감소 메세지 전송 실패 : {}", e.getMessage());
            order.updateStatus(OrderStatus.CANCEL);
            orderRepository.saveAndFlush(order);
            throw new ApplicationException(ErrorCode.PRODUCT_INVALID_VALUE_EXCEPTION);
        }
    }

    public void sendCancelPaymentMessage(Order order) {
        try {
            orderEventProducer.sendCancelPaymentMessage(order);
        } catch (Exception e) {
            log.error("결제 취소 요청 메세지 전송 실패 : {}", e.getMessage());
            order.updateStatus(OrderStatus.PENDING);
            orderRepository.saveAndFlush(order);
            throw new ApplicationException(ErrorCode.ORDER_NOT_CANCELLABLE_EXCEPTION);
        }
    }

    public void sendRefundPaymentMessage(Order order) {
        try {
            orderEventProducer.sendRefundPaymentMessage(order);
        } catch (Exception e) {
            log.error("결제 환불 메세지 전송 실패 : {}", e.getMessage());
        }
    }
}
