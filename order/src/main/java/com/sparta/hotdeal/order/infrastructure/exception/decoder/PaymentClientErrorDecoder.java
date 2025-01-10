package com.sparta.hotdeal.order.infrastructure.exception.decoder;

import com.sparta.hotdeal.order.common.exception.ApplicationException;
import com.sparta.hotdeal.order.common.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class PaymentClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            //결제 쪽 에러처리 어떻게 해야할지 생각이 필요.
            case 400 -> new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
            case 404 -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
            default -> new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        };
    }
}
