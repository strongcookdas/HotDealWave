package com.sparta.hotdeal.payment.infrastructure.exception.decoder;

import com.sparta.hotdeal.payment.common.exception.ApplicationException;
import com.sparta.hotdeal.payment.common.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class OrderClientErrorDecoder  implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
            case 404 -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
            default -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        };
    }
}
