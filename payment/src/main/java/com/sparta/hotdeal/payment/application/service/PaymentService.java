package com.sparta.hotdeal.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.dtos.order.ReqOrderUpdateStatusMessage;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentByIdDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentForListDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentCancelDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentRefundDto;
import com.sparta.hotdeal.payment.application.exception.ApplicationException;
import com.sparta.hotdeal.payment.application.exception.ErrorCode;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import com.sparta.hotdeal.payment.event.producer.PaymentEventProducer;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    @Value("${spring.kafka.topics.update-order-status}")
    private String updateOrderStatusTopic;

    private final PaymentRepository paymentRepository;

    private final KakaoPayClientPort kakaoPayClientPort;
    private final PaymentEventProducer paymentEventProducer;
    private final ObjectMapper objectMapper;

    /* 테스트 후 삭제
    public ResPostPaymentsDto readyPayment(UUID userId, ReqPostPaymentDto reqPostPaymentDto) {
        KakaoPayReadyDto kakaoPayReadyDto = kakaoPayClientPort.ready(userId, reqPostPaymentDto);
        Payment payment = Payment.create(
                reqPostPaymentDto.getOrderId(),
                userId,
                PaymentStatus.PENDING,
                reqPostPaymentDto.getTotalAmount(),
                0,
                kakaoPayReadyDto.getTid(),
                kakaoPayReadyDto.getNext_redirect_pc_url()
        );
        paymentRepository.save(payment);
        return ResPostPaymentsDto.of(kakaoPayReadyDto);
    }*/

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

    public ResPostPaymentConfirmDto approvePayment(UUID userId, ReqPostPaymentConfirmDto reqPostPaymentConfirmDto) {
        Payment payment = paymentRepository.findByTid(reqPostPaymentConfirmDto.getTid())
                .orElseThrow(() -> new ApplicationException(ErrorCode.PAYMENT_NOT_FOUND_EXCEPTION));
        KakaoPayApproveDto kakaoPayApproveDto = kakaoPayClientPort.approve(userId, reqPostPaymentConfirmDto,
                PaymentDto.from(payment));

        payment.updateStatus(PaymentStatus.COMPLETE);
        sendUpdateOrderStatusMessage(payment, OrderStatus.COMPLETE.toValue());

        return ResPostPaymentConfirmDto.of(kakaoPayApproveDto);
    }

    @Transactional(readOnly = true)
    public ResGetPaymentByIdDto getPaymentById(UUID userId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PAYMENT_NOT_FOUND_EXCEPTION));
        return ResGetPaymentByIdDto.of(payment);
    }

    @Transactional(readOnly = true)
    public Page<ResGetPaymentForListDto> getPaymentList(UUID userId, Pageable pageable) {
        return paymentRepository.findAllByUserId(userId, pageable).map(ResGetPaymentForListDto::of);
    }

    public ResPostPaymentRefundDto refundPayment(UUID userId, UUID orderId) {
        Payment payment = getPaymentByOrderIdAndUserId(userId, orderId);
        checkPaymentStatusRefundable(payment);

        KakaoPayCancelDto kakaoPayCancelDto = kakaoPayClientPort.cancel(PaymentDto.from(payment));
        payment.updateRefundInfo(kakaoPayCancelDto.getApprovedCancelAmount().getTotal());

        sendUpdateOrderStatusMessage(payment, OrderStatus.REFUND.toValue());
        return ResPostPaymentRefundDto.from(kakaoPayCancelDto);
    }

    private void checkPaymentStatusRefundable(Payment payment) {
        if (!payment.getStatus().equals(PaymentStatus.COMPLETE)) {
            throw new ApplicationException(ErrorCode.PAYMENT_CAN_NOT_REFUND);
        }
    }

    public ResPostPaymentCancelDto cancelPayment(UUID userId, UUID orderId) {
        Payment payment = getPaymentByOrderIdAndUserId(userId, orderId);
        checkPaymentStatusCancelable(payment);

        payment.updateStatus(PaymentStatus.CANCEL);

        sendUpdateOrderStatusMessage(payment, OrderStatus.CANCEL.toValue());
        return ResPostPaymentCancelDto.from(payment);
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

    private void sendUpdateOrderStatusMessage(Payment payment, String status) {
        try {
            ReqOrderUpdateStatusMessage reqOrderUpdateStatusMessage = ReqOrderUpdateStatusMessage.of(
                    payment.getOrderId(),
                    status);
            String message = objectMapper.writeValueAsString(reqOrderUpdateStatusMessage);
            paymentEventProducer.sendMessage(updateOrderStatusTopic, payment.getOrderId().toString(), message);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
