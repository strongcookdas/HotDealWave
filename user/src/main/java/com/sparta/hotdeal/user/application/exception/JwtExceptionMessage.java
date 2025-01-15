package com.sparta.hotdeal.user.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionMessage {
    INVALID_JWT_SIGNATURE("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다."),
    EXPIRED_JWT_TOKEN("Expired JWT token, 만료된 JWT token 입니다."),
    UNSUPPORTED_JWT_TOKEN("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다."),
    JWT_CLAIM_IS_EMPTY("JWT claims is empty, 잘못된 JWT 토큰 입니다.");

    private final String message;
}
