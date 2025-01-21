package com.sparta.hotdeal.order.application.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseDto<T> {
    private String message;
    private T data;

    public static <T> ResponseDto<T> of(String message, T data) {
        return ResponseDto.<T>builder()
                .message(message)
                .data(data)
                .build();
    }
}
