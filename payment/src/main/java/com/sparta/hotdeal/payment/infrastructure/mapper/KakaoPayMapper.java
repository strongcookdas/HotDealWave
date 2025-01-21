package com.sparta.hotdeal.payment.infrastructure.mapper;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayCancelDto;
import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayCancelDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayApproveDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayReadyDto;

public class KakaoPayMapper {

    public static KakaoPayReadyDto tokakaoPayReadyDto(ResPostKakaoPayReadyDto dto) {
        return KakaoPayReadyDto.builder()
                .tid(dto.getTid())
                .next_redirect_pc_url(dto.getNext_redirect_pc_url())
                .created_at(dto.getCreated_at())
                .build();
    }

    public static KakaoPayApproveDto toKakaoPayApproveDto(ResPostKakaoPayApproveDto dto) {
        return KakaoPayApproveDto.builder()
                .aid(dto.getAid())
                .tid(dto.getTid())
                .cid(dto.getCid())
                .status(dto.getStatus())
                .payment_method_type(dto.getPayment_method_type())
                .approved_at(dto.getApproved_at())
                .amount(KakaoPayApproveDto.Amount.builder()
                        .total(dto.getAmount().getTotal())
                        .tax_free(dto.getAmount().getTax_free())
                        .vat(dto.getAmount().getVat())
                        .point(dto.getAmount().getPoint())
                        .discount(dto.getAmount().getDiscount())
                        .build()).build();
    }

    public static KakaoPayCancelDto toKakaoPayCancelDto(ResPostKakaoPayCancelDto dto) {
        return KakaoPayCancelDto.builder()
                .tid(dto.getTid())
                .cid(dto.getCid())
                .status(dto.getStatus())
                .partnerOrderId(dto.getPartnerOrderId())
                .partnerUserId(dto.getPartnerUserId())
                .paymentMethodType(dto.getPaymentMethodType())
                .itemName(dto.getItemName())
                .quantity(dto.getQuantity())
                .amount(KakaoPayCancelDto.Amount.builder()
                        .total(dto.getAmount().getTotal())
                        .taxFree(dto.getAmount().getTaxFree())
                        .vat(dto.getAmount().getVat())
                        .point(dto.getAmount().getPoint())
                        .discount(dto.getAmount().getDiscount())
                        .greenDeposit(dto.getAmount().getGreenDeposit())
                        .build()
                )
                .approvedCancelAmount(KakaoPayCancelDto.Amount.builder()
                        .total(dto.getApprovedCancelAmount().getTotal())
                        .taxFree(dto.getApprovedCancelAmount().getTaxFree())
                        .vat(dto.getApprovedCancelAmount().getVat())
                        .point(dto.getApprovedCancelAmount().getPoint())
                        .discount(dto.getApprovedCancelAmount().getDiscount())
                        .greenDeposit(dto.getApprovedCancelAmount().getGreenDeposit())
                        .build()
                )
                .canceledAmount(KakaoPayCancelDto.Amount.builder()
                        .total(dto.getCanceledAmount().getTotal())
                        .taxFree(dto.getCanceledAmount().getTaxFree())
                        .vat(dto.getCanceledAmount().getVat())
                        .point(dto.getCanceledAmount().getPoint())
                        .discount(dto.getCanceledAmount().getDiscount())
                        .greenDeposit(dto.getCanceledAmount().getGreenDeposit())
                        .build()
                )
                .cancelAvailableAmount(KakaoPayCancelDto.Amount.builder()
                        .total(dto.getCancelAvailableAmount().getTotal())
                        .taxFree(dto.getCancelAvailableAmount().getTaxFree())
                        .vat(dto.getCancelAvailableAmount().getVat())
                        .point(dto.getCancelAvailableAmount().getPoint())
                        .discount(dto.getCancelAvailableAmount().getDiscount())
                        .greenDeposit(dto.getCancelAvailableAmount().getGreenDeposit())
                        .build()
                )
                .createdAt(dto.getCreatedAt())
                .approvedAt(dto.getApprovedAt())
                .canceledAt(dto.getCanceledAt())
                .build();
    }
}
