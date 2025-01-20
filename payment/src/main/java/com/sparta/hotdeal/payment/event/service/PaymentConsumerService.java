package com.sparta.hotdeal.payment.event.service;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentCancelDto;
import com.sparta.hotdeal.payment.common.exception.ApplicationException;
import com.sparta.hotdeal.payment.common.exception.ErrorCode;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentConsumerService {
    private final PaymentRepository paymentRepository;
    private final KakaoPayClientPort kakaoPayClientPort;

    public void readyPayment(OrderDto orderDto) {
        KakaoPayReadyDto kakaoPayReadyDto = kakaoPayClientPort.readyPayment(orderDto);
        Payment payment = Payment.create(
                orderDto.getOrderId(),
                orderDto.getUserId(),
                PaymentStatus.PENDING,
                orderDto.getTotalAmount(),
                0,
                kakaoPayReadyDto.getTid(),
                kakaoPayReadyDto.getNext_redirect_pc_url()
        );
        paymentRepository.save(payment);
    }

    public void cancelPayment(UUID userId, UUID orderId) {
        Payment payment = getPaymentByOrderIdAndUserId(userId, orderId);
        checkPaymentStatusCancelable(payment);

        payment.updateStatus(PaymentStatus.CANCEL);
        ResPostPaymentCancelDto.from(payment);
    }

    private Payment getPaymentByOrderIdAndUserId(UUID userId, UUID orderId) {
        return paymentRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PAYMENT_NOT_FOUND_EXCEPTION));
    }

    private void checkPaymentStatusCancelable(Payment payment) {
        if (!payment.getStatus().equals(PaymentStatus.PENDING)) {
            throw new ApplicationException(ErrorCode.PAYMENT_CAN_NOT_CANCEL);
        }
    }

    public void refundPayment(UUID userId, UUID orderId) {
        Payment payment = getPaymentByOrderIdAndUserId(userId, orderId);
        checkPaymentStatusRefundable(payment);

        KakaoPayCancelDto kakaoPayCancelDto = kakaoPayClientPort.cancel(PaymentDto.from(payment));
        payment.updateRefundInfo(kakaoPayCancelDto.getApprovedCancelAmount().getTotal());
    }

    private void checkPaymentStatusRefundable(Payment payment) {
        if (!payment.getStatus().equals(PaymentStatus.COMPLETE)) {
            throw new ApplicationException(ErrorCode.PAYMENT_CAN_NOT_REFUND);
        }
    }
}
