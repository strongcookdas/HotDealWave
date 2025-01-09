package com.sparta.hotdeal.order.infrastructure.exception.decoder;

import com.sparta.hotdeal.order.application.exception.ApplicationException;
import com.sparta.hotdeal.order.application.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CouponClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new ApplicationException(ErrorCode.COUPON_INVALID_VALUE_EXCEPTION);
            case 404 -> new ApplicationException(ErrorCode.COUPON_NOT_FOUND_EXCEPTION);
            default -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        };
    }
}
