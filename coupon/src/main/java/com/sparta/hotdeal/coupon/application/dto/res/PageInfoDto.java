package com.sparta.hotdeal.coupon.application.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoDto {
    private int size;         // 페이지 크기
    private int number;       // 현재 페이지 번호
    private long totalElements; // 전체 요소 수
    private int totalPages;   // 전체 페이지 수
}
