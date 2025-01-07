package com.sparta.hotdeal.payment.infrastructure.adapter;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.res.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.infrastructure.client.KakaoPayClient;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayReadyDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayReadyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoPayClientAdapter implements KakaoPayClientPort {

    private final KakaoPayClient kakaoPayClient;

    @Value("${pay.key}")
    private String authorization;

    @Override
    public KakaoPayReadyDto ready(ReqPostPaymentDto reqPostPaymentDto) {
        ReqPostKakaoPayReadyDto reqPostKakaoPayReadyDto = ReqPostKakaoPayReadyDto.create(
                reqPostPaymentDto.getOrderId(),
                reqPostPaymentDto.getUserId(),
                reqPostPaymentDto.getOrderName(),
                reqPostPaymentDto.getQuantity(),
                reqPostPaymentDto.getTotalAmount()
        );
        ResPostKakaoPayReadyDto resPostKakaoPayReadyDto = kakaoPayClient.ready(authorization, reqPostKakaoPayReadyDto);
        KakaoPayReadyDto kakaoPayReadyDto = KakaoPayReadyDto.create(
                resPostKakaoPayReadyDto.getTid(),
                resPostKakaoPayReadyDto.getNext_redirect_pc_url(),
                resPostKakaoPayReadyDto.getCreated_at()
        );
        return kakaoPayReadyDto;
    }
}
