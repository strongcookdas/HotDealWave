package com.sparta.hotdeal.payment.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    public ErrorCode errorCode;
}
