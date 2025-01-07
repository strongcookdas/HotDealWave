package com.sparta.hotdeal.coupon.application.dto;

import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {

    private LocalDateTime timestamp;
    private String code;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode, String customMessage) {
        this(LocalDateTime.now(), errorCode.getCode(), customMessage != null ? customMessage : errorCode.getDescription());
    }
}
