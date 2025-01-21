package com.sparta.hotdeal.payment.application.dtos.kakaopay;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayReadyDto {
    private String tid;                // 결제 고유 번호
    private String next_redirect_pc_url;  // PC 웹 결제 페이지 URL
    private String created_at;
}
