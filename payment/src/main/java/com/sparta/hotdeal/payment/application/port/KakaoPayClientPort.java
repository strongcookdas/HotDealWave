package com.sparta.hotdeal.payment.application.port;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.order.OrderDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import java.util.UUID;

public interface KakaoPayClientPort {
    KakaoPayReadyDto ready(UUID userId, ReqPostPaymentDto reqPostPaymentDto);
    KakaoPayReadyDto readyPayment(OrderDto orderDto);

    KakaoPayApproveDto approve(UUID userId, ReqPostPaymentConfirmDto paymentConfirmDto, PaymentDto paymentDto);

    KakaoPayCancelDto cancel(PaymentDto paymentDto);
}
