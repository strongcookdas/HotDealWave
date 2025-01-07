package com.sparta.hotdeal.payment.application.service;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentsDto;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.entity.payment.PaymentStatus;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final KakaoPayClientPort kakaoPayClientPort;
    private final PaymentRepository paymentRepository;

    public ResPostPaymentsDto readyPayment(ReqPostPaymentDto reqPostPaymentDto) {
        KakaoPayReadyDto kakaoPayReadyDto = kakaoPayClientPort.ready(reqPostPaymentDto);
        return ResPostPaymentsDto.of(kakaoPayReadyDto);
    }

    public ResPostPaymentConfirmDto approvePayment(UUID userId, ReqPostPaymentConfirmDto reqPostPaymentConfirmDto) {
        Payment payment = paymentRepository.findByTid(reqPostPaymentConfirmDto.getTid())
                .orElseThrow(() -> new IllegalArgumentException("결제 정보 없음"));
        KakaoPayApproveDto kakaoPayApproveDto = kakaoPayClientPort.approve(userId, reqPostPaymentConfirmDto,
                PaymentDto.from(payment));
        payment.updateStatus(PaymentStatus.COMPLETE);
        return ResPostPaymentConfirmDto.of(kakaoPayApproveDto);
    }
}
