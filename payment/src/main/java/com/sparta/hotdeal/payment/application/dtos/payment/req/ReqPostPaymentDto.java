package com.sparta.hotdeal.payment.application.dtos.payment.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ReqPostPaymentDto {
    @NotNull(message = "orderId는 필수 값입니다.")
    private UUID orderId;
    @NotNull(message = "userId는 필수 값입니다.")
    private UUID userId;
    @NotBlank(message = "orderName은 공백일 수 없습니다.")
    private String orderName;
    @NotNull(message = "quantity는 필수 값입니다.")
    private Integer quantity;
    @NotNull(message = "totalAmount는 필수 값입니다.")
    private Integer totalAmount;
}
