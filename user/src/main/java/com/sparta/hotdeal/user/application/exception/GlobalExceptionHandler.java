package com.sparta.hotdeal.user.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "AuthExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> illegalArgumentException(final IllegalArgumentException ex) {

        log.warn(ex.getMessage());

        RestApiException restApiException = RestApiException.builder()
                .errorMessage(ex.getMessage())
                .build();

        return new ResponseEntity<>(
                restApiException,
                HttpStatus.BAD_REQUEST
        );
    }

    // @Valid 유효성 검사 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiException> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessages.append(error.getField())
                        .append(" : ")
                        .append(error.getDefaultMessage())
                        .append(" ")
        );

        RestApiException restApiException = new RestApiException(errorMessages.toString());

        return new ResponseEntity<>(
                restApiException,
                HttpStatus.BAD_REQUEST);
    }
}
