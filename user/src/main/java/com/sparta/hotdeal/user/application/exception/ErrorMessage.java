package com.sparta.hotdeal.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    DUPLICATED_NICKNAME("중복된 닉네임이 있습니다."),
    DUPLICATED_EMAIL("중복된 이메일이 있습니다."),
    NOT_ALLOWED_ROLE("MANAGER 또는 MASTER 권한으로 가입할 수 없습니다."),
    SEND_EMAIL_ERROR("이메일 전송에 실패했습니다."),
    WRONG_VERIFICATION_CODE("인증 코드가 일치하지 않거나 만료되었습니다."),
    WRONG_EMAIL_OR_PASSWORD("이메일 또는 비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_VERIFIED("이메일 인증을 먼저 진행해주세요."),
    USER_NOT_FOUND("회원을 찾을 수 없습니다.");

    private final String message;
}
