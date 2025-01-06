package com.sparta.hotdeal.order.application.exception;

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

    //Order
    ORDER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "O001", "존재하지 않는 주문 리소스입니다."),
    ORDER_INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "O002", "올바르지 않은 주문 요청 값입니다."),
    ORDER_UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "O003", "주문에 대한 권한이 없는 요청입니다."),
    ORDER_ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, "O004", "이미 삭제된 주문 리소스입니다."),
    ORDER_FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "O005", "인가되지 않는 주문 요청입니다."),
    ORDER_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "O006", "이미 존재하는 주문 리소스입니다."),

    //Product
    PRODUCT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "OP001", "존재하지 않는 상품 리소스입니다."),
    PRODUCT_FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "OP002", "인가되지 않는 상품 요청입니다."),
    PRODUCT_NOT_ON_SALE_EXCEPTION(HttpStatus.BAD_REQUEST,"OP003","판매하지 않는 상품입니다."),
    PRODUCT_INVALID_QUANTITY_EXCEPTION(HttpStatus.BAD_REQUEST,"OP004","상품 수량이 부족합니다."),

    //Basket
    BASKET_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "OB001", "존재하지 않는 장바구니 리소스입니다."),
    BASKET_INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "OB002", "올바르지 않은 장바구니 요청 값입니다."),
    BASKET_UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "OB003", "장바구니에 대한 권한이 없는 요청입니다."),
    BASKET_ALREADY_DELETE_EXCEPTION(HttpStatus.BAD_REQUEST, "OB004", "이미 삭제된 장바구니 리소스입니다."),
    BASKET_FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "OB005", "인가되지 않는 장바구니 요청입니다."),
    BASKET_ALREADY_EXIST_EXCEPTION(HttpStatus.BAD_REQUEST, "OB006", "이미 존재하는 장바구니 리소스입니다."),

    //Coupon
    COUPON_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "OCO001", "존재하지 않는 쿠폰 리소스입니다."),
    COUPON_ALREADY_USED_EXCEPTION(HttpStatus.BAD_REQUEST, "OC002", "이미 사용한 쿠폰입니다."),
    COUPON_MINIMUM_PRICE_EXCEPTION(HttpStatus.BAD_REQUEST, "OC003", "쿠폰을 사용할 수 있는 최소금액이 아닙니다."),
    COUPON_INVALID_COMPANY_EXCEPTION(HttpStatus.BAD_REQUEST, "OC004", "쿠폰을 사용할 수 있는 회사 상품이 없습니다."),
    COUPON_EXPIRED_EXCEPTION(HttpStatus.BAD_REQUEST, "OC005", "만료된 쿠폰입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
