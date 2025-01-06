package com.sparta.hotdeal.order.application.exception;

import com.sparta.hotdeal.order.application.dtos.ErrorResponseDto;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("Access Denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(ErrorCode.FORBIDDEN_EXCEPTION, "Access is denied"));
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ErrorResponseDto> handleApplicationException(ApplicationException e){
        log.error("{} {}", e, e.getErrorCode().toString());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ErrorResponseDto(e.getErrorCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(e.getMessage()));
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
