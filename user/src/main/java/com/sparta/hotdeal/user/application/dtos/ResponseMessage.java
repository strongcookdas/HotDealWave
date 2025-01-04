package com.sparta.hotdeal.user.application.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SIGNUP_SUCCESS("회원가입 성공"),
    USABLE_EMAIL("사용할 수 있는 이메일입니다.");

    private final String message;
}
