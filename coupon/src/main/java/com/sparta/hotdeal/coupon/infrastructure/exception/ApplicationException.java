package com.sparta.hotdeal.coupon.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    public ApplicationErrorCode errorCode;
}