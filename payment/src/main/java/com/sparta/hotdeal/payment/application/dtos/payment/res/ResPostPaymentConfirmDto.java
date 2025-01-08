package com.sparta.hotdeal.payment.application.dtos.payment.res;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayApproveDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostPaymentConfirmDto {
    private String aid;                // 요청 고유 번호
    private String tid;                // 결제 고유 번호
    private String cid;                // 가맹점 코드
    private String status;             // 결제 상태
    private String payment_method_type; // 결제 수단
    private String approved_at;        // 결제 승인 시간
    private Amount amount;             // 결제 금액 정보

    @Getter
    @Builder
    public static class Amount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;
    }

    public static ResPostPaymentConfirmDto of(KakaoPayApproveDto kakaoPayApproveDto) {
        return ResPostPaymentConfirmDto.builder()
                .aid(kakaoPayApproveDto.getAid()) // 요청 고유 번호
                .tid(kakaoPayApproveDto.getTid()) // 결제 고유 번호
                .cid(kakaoPayApproveDto.getCid()) // 가맹점 코드
                .status(kakaoPayApproveDto.getStatus()) // 결제 상태
                .payment_method_type(kakaoPayApproveDto.getPayment_method_type()) // 결제 수단
                .approved_at(kakaoPayApproveDto.getApproved_at()) // 결제 승인 시간
                .amount(ResPostPaymentConfirmDto.Amount.builder()
                        .total(kakaoPayApproveDto.getAmount().getTotal()) // 총 금액
                        .tax_free(kakaoPayApproveDto.getAmount().getTax_free()) // 비과세 금액
                        .vat(kakaoPayApproveDto.getAmount().getVat()) // 부가세 금액
                        .point(kakaoPayApproveDto.getAmount().getPoint()) // 포인트 사용 금액
                        .discount(kakaoPayApproveDto.getAmount().getDiscount()) // 할인 금액
                        .build()
                )
                .build();
    }
}
