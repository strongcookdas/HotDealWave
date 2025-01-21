package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostKakaoPayApproveDto {
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
}
