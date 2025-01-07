package com.sparta.hotdeal.payment.application.port;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.res.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;

public interface KakaoPayClientPort {
    KakaoPayReadyDto ready(ReqPostPaymentDto reqPostPaymentDto);
}
