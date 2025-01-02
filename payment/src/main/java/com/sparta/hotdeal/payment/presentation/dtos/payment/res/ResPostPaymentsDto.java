package com.sparta.hotdeal.payment.presentation.dtos.payment.res;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPostPaymentsDto {
    private String tid;
    private String nextRedirectPcUrl;
    private LocalDateTime createdAt;

    public static ResPostPaymentsDto createDummyData() {
        return ResPostPaymentsDto.builder()
                .tid("tid")
                .nextRedirectPcUrl("url")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
