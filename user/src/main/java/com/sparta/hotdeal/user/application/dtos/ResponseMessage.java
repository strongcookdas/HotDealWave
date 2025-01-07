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
    UPDATE_USER_SUCCESS("회원정보수정 성공"),
    UPDATE_USER_PASSWORD_SUCCESS("비밀번호수정 성공"),
    DELETE_USER_SUCCESS("회원탈퇴 성공"),
    CREATE_ADDRESS_SUCCESS("배송지 등록 성공"),
    GET_ADDRESS_SUCCESS("배송지 조회 성공"),
    GET_DEFAULT_ADDRESS_SUCCESS("기본 배송지 조회 성공"),
    GET_ADDRESS_LIST_SUCCESS("배송지 목록 조회 성공"),
    UPDATE_ADDRESS_SUCCESS("배송지 수정 성공"),
    UPDATE_DEFAULT_ADDRESS_SUCCESS("기본 배송지 설정 성공"),
    DELETE_ADDRESS_SUCCESS("배송지 삭제 성공");

    private final String message;
}
