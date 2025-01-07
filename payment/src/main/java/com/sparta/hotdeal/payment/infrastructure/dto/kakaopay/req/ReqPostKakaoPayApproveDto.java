package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostKakaoPayApproveDto {
    private String cid;                // 가맹점 코드
    private String tid;                // 결제 고유 번호
    private String partner_order_id;   // 가맹점 주문번호
    private String partner_user_id;    // 가맹점 회원 ID
    private String pg_token;           // 결제승인 요청 인증 토큰

    public static ReqPostKakaoPayApproveDto create(
            String cid,
            String tid,
            String partner_order_id,
            String partner_user_id,
            String pg_token
    ) {
        return ReqPostKakaoPayApproveDto.builder()
                .cid(cid)
                .tid(tid)
                .partner_order_id(partner_order_id)
                .partner_user_id(partner_user_id)
                .pg_token(pg_token)
                .build();
    }
}
