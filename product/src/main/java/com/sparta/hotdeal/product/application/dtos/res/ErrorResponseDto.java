package com.sparta.hotdeal.product.application.dtos.res;

import com.sparta.hotdeal.product.application.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private LocalDateTime timestamp;
    private Integer code;
    private String message;

    public ErrorResponseDto(ErrorCode errorcode) {
        this(LocalDateTime.now(), errorcode.getCode(), errorcode.getMessage());
    }

    public ErrorResponseDto(String message) {
        this(LocalDateTime.now(), ErrorCode.INTERNAL_SERVER_EXCEPTION.getCode(), message);
    }

    public ErrorResponseDto(ErrorCode errorcode, String message) {
        this(LocalDateTime.now(), errorcode.getCode(), message);
    }
}