package com.sparta.hotdeal.payment.infrastructure.client;

import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayReadyDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayReadyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoPayClient", url = "https://kapi.kakao.com/v1/payment")
public interface KakaoPayClient {
    @PostMapping("/ready")
    ResPostKakaoPayReadyDto ready(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReqPostKakaoPayReadyDto request
    );
}
