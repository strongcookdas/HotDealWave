package com.sparta.hotdeal.order.application.dtos.basket.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqPostBasketDto {

    @NotNull(message = "Product ID는 필수 값입니다.")
    private UUID productId;

    @NotNull(message = "Quantity는 필수 값입니다.")
    @Min(value = 1, message = "Quantity는 1 이상이어야 합니다.")
    private Integer quantity;
}
