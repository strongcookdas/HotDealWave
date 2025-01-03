package com.sparta.hotdeal.coupon.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND_COUPONINFO(HttpStatus.NOT_FOUND, "CI001", "해당 쿠폰 정보 ID가 존재하지 않습니다."),
    ALREADY_SET_COUPONINFO_STATUS(HttpStatus.BAD_REQUEST, "CI002", "이미 쿠폰이 해당 상태로 설정되어 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String description;


    ErrorCode(HttpStatus status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
