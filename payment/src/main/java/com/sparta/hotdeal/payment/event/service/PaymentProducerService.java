package com.sparta.hotdeal.payment.event.service;

import com.sparta.hotdeal.payment.common.exception.ApplicationException;
import com.sparta.hotdeal.payment.common.exception.ErrorCode;
import com.sparta.hotdeal.payment.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import com.sparta.hotdeal.payment.event.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentProducerService {

    private final PaymentEventProducer paymentEventProducer;
    private final PaymentRepository paymentRepository;

    public void sendUpdateOrderStatusMessage(Payment payment, OrderStatus orderStatus) {
        try {
            paymentEventProducer.sendUpdateOrderStatusMessage(payment, orderStatus);
        } catch (Exception e) {
            payment.updateStatus(PaymentStatus.CANCEL);
            paymentRepository.saveAndFlush(payment);
            throw new ApplicationException(ErrorCode.PAYMENT_CAN_NOT_APPROVE);
        }
    }
}

