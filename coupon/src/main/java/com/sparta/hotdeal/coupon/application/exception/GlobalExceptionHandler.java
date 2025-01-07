package com.sparta.hotdeal.coupon.application.exception;

import com.sparta.hotdeal.coupon.application.dto.ErrorResponseDto;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseDto> handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getErrorCode());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponseDto(e.getErrorCode(), e.getErrorCode().getDescription()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Access Denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(ErrorCode.UNAUTHORIZED_ACCESS, "Access is denied"));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(
                        ErrorCode.INVALID_VALUE_EXCEPTION,
                        Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }
}
