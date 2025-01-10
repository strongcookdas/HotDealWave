package com.sparta.hotdeal.payment.infrastructure.adapter;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.application.dtos.payment.PaymentDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentConfirmDto;
import com.sparta.hotdeal.payment.application.dtos.payment.req.ReqPostPaymentDto;
import com.sparta.hotdeal.payment.application.port.KakaoPayClientPort;
import com.sparta.hotdeal.payment.infrastructure.client.KakaoPayClient;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayApproveDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayReadyDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayApproveDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayReadyDto;
import com.sparta.hotdeal.payment.infrastructure.mapper.KakaoPayMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoPayClientAdapter implements KakaoPayClientPort {

    private final KakaoPayClient kakaoPayClient;

    @Value("${pay.key}")
    private String authorization;
    @Value("${pay.cid}")
    private String cid;
    @Value("${pay.host.domain}")
    private String domain;

    @Override
    public KakaoPayReadyDto ready(UUID userId, ReqPostPaymentDto reqPostPaymentDto) {
        ReqPostKakaoPayReadyDto reqPostKakaoPayReadyDto = ReqPostKakaoPayReadyDto.create(
                cid,
                reqPostPaymentDto.getOrderId(),
                userId,
                reqPostPaymentDto.getOrderName(),
                reqPostPaymentDto.getQuantity(),
                reqPostPaymentDto.getTotalAmount(),
                domain
        );
        ResPostKakaoPayReadyDto resPostKakaoPayReadyDto = kakaoPayClient.ready(authorization, reqPostKakaoPayReadyDto);
        KakaoPayReadyDto kakaoPayReadyDto = KakaoPayReadyDto.create(
                resPostKakaoPayReadyDto.getTid(),
                resPostKakaoPayReadyDto.getNext_redirect_pc_url(),
                resPostKakaoPayReadyDto.getCreated_at()
        );
        return kakaoPayReadyDto;
    }

    @Override
    public KakaoPayApproveDto approve(UUID userId, ReqPostPaymentConfirmDto reqPostPaymentConfirmDto,
                                      PaymentDto paymentDto) {
        ReqPostKakaoPayApproveDto reqPostKakaoPayApproveDto = ReqPostKakaoPayApproveDto.create(
                cid,
                reqPostPaymentConfirmDto.getTid(),
                paymentDto.getOrderId().toString(),
                userId.toString(),
                reqPostPaymentConfirmDto.getPgToken()
        );

        //복잡한 변환만 mapper로 일단 분리
        ResPostKakaoPayApproveDto resPostKakaoPayApproveDto = kakaoPayClient.approve(authorization,
                reqPostKakaoPayApproveDto);

        return KakaoPayMapper.toKakaoPayApproveDto(resPostKakaoPayApproveDto);
    }
}
