package com.sparta.hotdeal.coupon.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND_COUPON_INFO(HttpStatus.NOT_FOUND, "CO001", "해당 쿠폰 정보 ID가 존재하지 않습니다."),
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, "CO002", "해당 쿠폰 ID가 존재하지 않습니다."),
    ALREADY_SET_COUPON_INFO_STATUS(HttpStatus.BAD_REQUEST, "CO003", "이미 쿠폰이 해당 상태로 설정되어 있습니다."),
    INVALID_COUPON_STATUS(HttpStatus.BAD_REQUEST, "CO004", "쿠폰 상태가 유효하지 않습니다."),
    COUPON_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "CO005", "쿠폰이 품절되었습니다."),
    ALREADY_ISSUED_COUPON(HttpStatus.BAD_REQUEST, "CO006", "사용자가 이미 해당 쿠폰을 발급받았습니다."),
    COUPON_ALREADY_USED(HttpStatus.BAD_REQUEST, "CO007", "이미 사용된 쿠폰입니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "CO008", "쿠폰이 만료되었습니다."),
    INVALID_COUPON_COMPANY(HttpStatus.BAD_REQUEST, "CO009", "해당 회사 제품에 사용할 수 없는 쿠폰입니다."),
    MIN_ORDER_AMOUNT_NOT_MET(HttpStatus.BAD_REQUEST, "CO010", "최소 주문 금액 조건을 충족하지 못했습니다."),
    COUPON_NOT_USED(HttpStatus.BAD_REQUEST, "CO011", "쿠폰이 사용된 상태가 아닙니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "CO012", "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CO013", "예기치 못한 오류가 발생했습니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "CO014", "올바르지 않은 요청 값입니다."),
    COMPANY_NOT_APPROVED(HttpStatus.BAD_REQUEST, "CO015", "회사가 APPROVED 상태가 아닙니다."),
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "CO016", "예기치 못한 오류가 발생했습니다."),
    COMPANY_INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "CO017", "올바르지 않은 업체 요청 값입니다."),
    COMPANY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "CO018", "존재하지 않는 업체입니다.");

    private final HttpStatus status;
    private final String code;
    private final String description;

    ErrorCode(HttpStatus status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
