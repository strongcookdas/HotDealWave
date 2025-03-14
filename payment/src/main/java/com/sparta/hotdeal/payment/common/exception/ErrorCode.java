package com.sparta.hotdeal.payment.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //common
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "001", "예기치 못한 오류가 발생했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "002", "존재하지 않는 리소스입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "003", "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "004", "권한이 없는 요청입니다."),
    ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, "005", "이미 삭제된 리소스입니다."),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "006", "인가되지 않는 요청입니다."),
    ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "007", "이미 존재하는 리소스입니다."),
    INVALID_SORT_EXCEPTION(HttpStatus.BAD_REQUEST, "008", "올바르지 않은 정렬 값입니다."),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "009", "존재하지 않는 사용자입니다."),

    //Payment
    PAYMENT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "PA001", "존재하지 않는 결제 리소스입니다."),
    PAYMENT_INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "PA002", "올바르지 않은 결제 요청 값입니다."),
    PAYMENT_CAN_NOT_CANCEL(HttpStatus.BAD_REQUEST, "PA003", "취소할 수 없는 결제 건입니다."),
    PAYMENT_CAN_NOT_REFUND(HttpStatus.BAD_REQUEST,"PA004", "환불할 수 없는 결제 건입니다."),
    PAYMENT_CAN_NOT_APPROVE(HttpStatus.BAD_REQUEST,"PA005","승인할 수 없는 결제 건입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
