package com.sparta.hotdeal.order.domain.entity.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    CREATE, PENDING, COMPLETE, CANCEL;

    @JsonCreator
    public static OrderStatus fromString(String status) {
        return OrderStatus.valueOf(status.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
