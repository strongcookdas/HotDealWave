package com.sparta.hotdeal.user.application.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SIGNUP_SUCCESS("회원가입 성공");

    private final String message;
}
