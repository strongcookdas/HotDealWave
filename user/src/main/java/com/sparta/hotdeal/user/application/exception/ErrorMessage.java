package com.sparta.hotdeal.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    DUPLICATED_NICKNAME("중복된 닉네임이 있습니다."),
    NOT_ALLOWED_ROLE("MANAGER 또는 MASTER 권한으로 가입할 수 없습니다.");

    private final String message;
}
