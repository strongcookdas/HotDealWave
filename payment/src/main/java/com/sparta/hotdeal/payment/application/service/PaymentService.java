package com.sparta.hotdeal.payment.application.service;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentByIdDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentForListDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentCancelDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentRefundDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentsDto;
import com.sparta.hotdeal.payment.common.exception.ApplicationException;
import com.sparta.hotdeal.payment.common.exception.ErrorCode;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.domain.entity.order.OrderStatus;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import com.sparta.hotdeal.payment.event.service.PaymentProducerService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final KakaoPayClientPort kakaoPayClientPort;

    private final PaymentProducerService paymentProducerService;

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
    }

    public ResPostPaymentConfirmDto approvePayment(UUID userId, ReqPostPaymentConfirmDto reqPostPaymentConfirmDto) {
        Payment payment = paymentRepository.findByTid(reqPostPaymentConfirmDto.getTid())
                .orElseThrow(() -> new ApplicationException(ErrorCode.PAYMENT_NOT_FOUND_EXCEPTION));
        KakaoPayApproveDto kakaoPayApproveDto = kakaoPayClientPort.approve(userId, reqPostPaymentConfirmDto,
                PaymentDto.from(payment));

        payment.updateStatus(PaymentStatus.COMPLETE);
        paymentProducerService.sendUpdateOrderStatusMessage(payment, OrderStatus.COMPLETE);

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
}
