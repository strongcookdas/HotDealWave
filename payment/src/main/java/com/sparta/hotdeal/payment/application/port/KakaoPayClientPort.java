package com.sparta.hotdeal.payment.application.port;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import java.util.UUID;

public interface KakaoPayClientPort {
    KakaoPayReadyDto ready(ReqPostPaymentDto reqPostPaymentDto);
    KakaoPayApproveDto approve(UUID userId, ReqPostPaymentConfirmDto paymentConfirmDto, PaymentDto paymentDto);
}
