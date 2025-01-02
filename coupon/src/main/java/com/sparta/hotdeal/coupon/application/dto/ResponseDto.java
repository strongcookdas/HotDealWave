package com.sparta.hotdeal.coupon.application.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ResponseDto<T> {
    private HttpStatus status;
    private String message;
    private T data;

    // 성공 응답
    public static <T> ResponseDto<T> ok(String message, T data) {
        return ResponseDto.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    // 생성 응답
    public static <T> ResponseDto<T> created(String message, T data) {
        return ResponseDto.<T>builder()
                .status(HttpStatus.CREATED)
                .message(message)
                .data(data)
                .build();
    }

    // 잘못된 요청 응답
    public static <T> ResponseDto<T> badRequest(String message) {
        return ResponseDto.<T>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(message)
                .data(null)
                .build();
    }

    // 커스텀 응답 (HTTP 상태를 직접 설정)
    public static <T> ResponseDto<T> of(HttpStatus status, String message, T data) {
        return ResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
