package com.sparta.hotdeal.order.infrastructure.exception.decoder;

import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ProductClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new ApplicationException(ErrorCode.PRODUCT_INVALID_VALUE_EXCEPTION);
            case 404 -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND_EXCEPTION);
            default -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        };
    }
}
