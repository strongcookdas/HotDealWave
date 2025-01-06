package com.sparta.hotdeal.order.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    public ErrorCode errorCode;
}
