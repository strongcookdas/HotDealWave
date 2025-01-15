package com.sparta.hotdeal.payment.infrastructure.mapper;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResDeleteKakaoPayCancelDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayApproveDto;

public class KakaoPayMapper {
    public static KakaoPayApproveDto toKakaoPayApproveDto(ResPostKakaoPayApproveDto resPostKakaoPayApproveDto) {
        return KakaoPayApproveDto.builder()
                .aid(resPostKakaoPayApproveDto.getAid())
                .tid(resPostKakaoPayApproveDto.getTid())
                .cid(resPostKakaoPayApproveDto.getCid())
                .status(resPostKakaoPayApproveDto.getStatus())
                .payment_method_type(resPostKakaoPayApproveDto.getPayment_method_type())
                .approved_at(resPostKakaoPayApproveDto.getApproved_at())
                .amount(KakaoPayApproveDto.Amount.builder()
                        .total(resPostKakaoPayApproveDto.getAmount().getTotal())
                        .tax_free(resPostKakaoPayApproveDto.getAmount().getTax_free())
                        .vat(resPostKakaoPayApproveDto.getAmount().getVat())
                        .point(resPostKakaoPayApproveDto.getAmount().getPoint())
                        .discount(resPostKakaoPayApproveDto.getAmount().getDiscount())
                        .build()).build();
    }
}
