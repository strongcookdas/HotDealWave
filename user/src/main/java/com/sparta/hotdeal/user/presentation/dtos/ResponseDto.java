package com.sparta.hotdeal.user.presentation.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseDto<T> {
    private Integer status;
    private String message;
    private T data;

    public static <T> ResponseDto<T> of(Integer status, String message, T data) {
        return ResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
