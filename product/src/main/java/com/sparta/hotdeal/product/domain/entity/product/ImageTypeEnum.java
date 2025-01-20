package com.sparta.hotdeal.product.domain.entity.product;

import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ImageTypeEnum {
    PRODUCT("product-image"),
    REVIEW("review-image");

    private final String value;

    ImageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ImageTypeEnum fromValue(String value) {
        for (ImageTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
    }
}