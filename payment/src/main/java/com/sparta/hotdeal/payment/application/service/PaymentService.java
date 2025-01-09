package com.sparta.hotdeal.payment.application.service;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentByIdDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResGetPaymentForListDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentsDto;
import com.sparta.hotdeal.payment.application.exception.ApplicationException;
import com.sparta.hotdeal.payment.application.exception.ErrorCode;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final KakaoPayClientPort kakaoPayClientPort;
    private final PaymentRepository paymentRepository;

    public ResPostPaymentsDto readyPayment(UUID userId, ReqPostPaymentDto reqPostPaymentDto) {
        KakaoPayReadyDto kakaoPayReadyDto = kakaoPayClientPort.ready(reqPostPaymentDto);
        Payment payment = Payment.create(
                reqPostPaymentDto.getOrderId(),
                userId,
                PaymentStatus.PENDING,
                reqPostPaymentDto.getTotalAmount(),
                0,
                kakaoPayReadyDto.getTid()
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
        return ResPostPaymentConfirmDto.of(kakaoPayApproveDto);
    }

    public ResGetPaymentByIdDto getPaymentById(UUID userId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PAYMENT_NOT_FOUND_EXCEPTION));
        return ResGetPaymentByIdDto.of(payment);
    }

    public Page<ResGetPaymentForListDto> getPaymentList(UUID userId, Pageable pageable) {
        return paymentRepository.findAllByUserId(userId, pageable).map(ResGetPaymentForListDto::of);
    }


}
