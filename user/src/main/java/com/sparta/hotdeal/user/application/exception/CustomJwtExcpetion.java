package com.sparta.hotdeal.user.application.exception;

public class CustomJwtExcpetion extends RuntimeException {
    public CustomJwtExcpetion(String message) {
        super(message);
    }
}
