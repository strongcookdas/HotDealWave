package com.sparta.hotdeal.coupon.application.dto;

import com.sparta.hotdeal.coupon.infrastructure.exception.ApplicationErrorCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private LocalDateTime timestamp;
    private Integer code;
    private String message;

    public ErrorResponseDto(ApplicationErrorCode errorcode) {
        this(LocalDateTime.now(), errorcode.getCode(), errorcode.getMessage());
    }

    public ErrorResponseDto(String message) {
        this(LocalDateTime.now(), ApplicationErrorCode.INTERNAL_SERVER_EXCEPTION.getCode(), message);
    }

    public ErrorResponseDto(ApplicationErrorCode errorcode, String message) {
        this(LocalDateTime.now(), errorcode.getCode(), message);
    }
}
