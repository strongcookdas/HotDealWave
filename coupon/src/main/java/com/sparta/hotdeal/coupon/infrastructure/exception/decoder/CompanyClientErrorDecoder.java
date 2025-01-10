package com.sparta.hotdeal.coupon.infrastructure.exception.decoder;

import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CompanyClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 400 -> new CustomException(ErrorCode.COMPANY_INVALID_VALUE_EXCEPTION);
            case 404 -> new CustomException(ErrorCode.COMPANY_NOT_FOUND_EXCEPTION);
            default -> new CustomException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        };
    }

}
