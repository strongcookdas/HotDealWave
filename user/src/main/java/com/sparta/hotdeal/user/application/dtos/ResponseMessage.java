package com.sparta.hotdeal.user.application.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SIGNUP_SUCCESS("회원가입 성공"),
    USABLE_EMAIL("사용할 수 있는 이메일입니다."),
    EMAIL_SENT("이메일 인증이 요청되었습니다. 메일을 확인해주세요."),
    EMAIL_CONFIRMED("이메일 인증이 처리되었습니다."),
    LOGIN_SUCCESS("로그인 성공"),
    GET_USER_SUCCESS("회원정보조회 성공"),
    UPDATE_USER_SUCCESS("회원정보수정 성공");

    private final String message;
}
