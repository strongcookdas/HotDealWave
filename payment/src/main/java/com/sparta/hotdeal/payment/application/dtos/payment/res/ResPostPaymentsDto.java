package com.sparta.hotdeal.payment.application.dtos.payment.res;

import com.sparta.hotdeal.payment.application.dtos.kakaopay.KakaoPayReadyDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPostPaymentsDto {
    private String tid;
    private String nextRedirectPcUrl;
    private String createdAt;

    public static ResPostPaymentsDto createDummyData() {
        return ResPostPaymentsDto.builder()
                .tid("tid")
                .nextRedirectPcUrl("url")
                .createdAt(LocalDateTime.now().toString())
                .build();
    }

    public static ResPostPaymentsDto of(KakaoPayReadyDto kakaoPayReadyDto){
        return ResPostPaymentsDto.builder()
                .tid(kakaoPayReadyDto.getTid())
                .nextRedirectPcUrl(kakaoPayReadyDto.getNext_redirect_pc_url())
                .createdAt(kakaoPayReadyDto.getCreated_at())
                .build();
    }
}
