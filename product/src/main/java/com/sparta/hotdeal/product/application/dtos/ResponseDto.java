package com.sparta.hotdeal.product.application.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto<T> {
    private Integer status;
    private String message;
    private T data;
}
