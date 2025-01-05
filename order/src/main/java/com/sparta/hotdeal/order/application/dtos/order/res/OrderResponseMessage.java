package com.sparta.hotdeal.order.application.dtos.order.res;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderResponseMessage {
    CREATE_ORDER("주문이 처리되었습니다.");

    private final String message;
}
