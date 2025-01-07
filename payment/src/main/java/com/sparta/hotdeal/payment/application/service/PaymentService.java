package com.sparta.hotdeal.payment.application.service;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.res.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.res.ResPostPaymentsDto;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final KakaoPayClientPort kakaoPayClientPort;

    public ResPostPaymentsDto readyPayment(ReqPostPaymentDto reqPostPaymentDto) {
        KakaoPayReadyDto kakaoPayReadyDto = kakaoPayClientPort.ready(reqPostPaymentDto);
        return ResPostPaymentsDto.of(kakaoPayReadyDto);
    }
}
