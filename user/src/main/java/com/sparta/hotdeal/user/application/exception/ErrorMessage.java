package com.sparta.hotdeal.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    DUPLICATED_NICKNAME("중복된 닉네임이 있습니다."),
    DUPLICATED_EMAIL("중복된 이메일이 있습니다."),
    NOT_ALLOWED_ROLE("MANAGER 또는 MASTER 권한으로 가입할 수 없습니다."),
    SEND_EMAIL_ERROR("이메일 전송에 실패했습니다.");

    private final String message;
}
