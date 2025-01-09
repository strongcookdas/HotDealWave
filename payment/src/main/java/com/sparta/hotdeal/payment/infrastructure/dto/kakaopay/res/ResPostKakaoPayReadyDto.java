package com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResPostKakaoPayReadyDto {
    private String tid;                // 결제 고유 번호
    private String next_redirect_pc_url;  // PC 웹 결제 페이지 URL
    private String next_redirect_mobile_url; // 모바일 결제 페이지 URL
    private String created_at;
}
