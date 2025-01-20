package com.sparta.hotdeal.payment.application.dtos.payment.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReqPostPaymentConfirmDto {
    @NotBlank(message = "pgToken은 공백일 수 없습니다.")
    private String pgToken;
    @NotBlank(message = "tid는 공백일 수 없습니다.")
    private String tid;
}
