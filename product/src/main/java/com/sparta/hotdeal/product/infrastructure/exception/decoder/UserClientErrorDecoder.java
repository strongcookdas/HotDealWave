package com.sparta.hotdeal.product.infrastructure.exception.decoder;

import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class UserClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400, 404 -> new ApplicationException(ErrorCode.USER_NOT_FOUND_EXCEPTION);
            default -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        };
    }
}
