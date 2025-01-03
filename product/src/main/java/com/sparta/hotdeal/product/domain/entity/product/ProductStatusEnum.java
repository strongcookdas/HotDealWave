package com.sparta.hotdeal.product.domain.entity.product;

public enum ProductStatusEnum {
    ON_SALE, // 판매중
    DISCONTINUED, // 판매 중지
    OUT_OF_STOCK,// 재고 부족
    SOLD_OUT, // 매진
    INACTIVE; // 비활성화 (임시로 판매 중단됨)
}
