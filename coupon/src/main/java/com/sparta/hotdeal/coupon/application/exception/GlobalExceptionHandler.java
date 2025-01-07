package com.sparta.hotdeal.coupon.application.exception;

import com.sparta.hotdeal.coupon.application.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ResponseDto<Void>> handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getErrorCode());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ResponseDto.of(e.getErrorCode().getDescription() + e.getErrorCode().getCode(), null));
    }
}
