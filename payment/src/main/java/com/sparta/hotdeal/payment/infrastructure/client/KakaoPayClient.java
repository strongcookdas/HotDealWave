package com.sparta.hotdeal.payment.infrastructure.client;

import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayCancelDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayApproveDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.req.ReqPostKakaoPayReadyDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayCancelDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayApproveDto;
import com.sparta.hotdeal.payment.infrastructure.dto.kakaopay.res.ResPostKakaoPayReadyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoPayClient", url = "https://open-api.kakaopay.com/online/v1/payment")
public interface KakaoPayClient {
    @PostMapping("/ready")
    ResPostKakaoPayReadyDto ready(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReqPostKakaoPayReadyDto request
    );

    @PostMapping("/approve")
    ResPostKakaoPayApproveDto approve(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReqPostKakaoPayApproveDto request
    );

    @PostMapping("/cancel")
    ResPostKakaoPayCancelDto cancel(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReqPostKakaoPayCancelDto request
    );

}
