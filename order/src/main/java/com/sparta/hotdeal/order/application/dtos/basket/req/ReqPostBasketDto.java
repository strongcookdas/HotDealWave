package com.sparta.hotdeal.order.application.dtos.basket.req;

import com.sparta.hotdeal.order.application.dtos.basket.CreateBasketDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReqPostBasketDto {

    @NotNull(message = "Product ID는 필수 값입니다.")
    private UUID productId;

    @NotNull(message = "Quantity는 필수 값입니다.")
    @Min(value = 1, message = "Quantity는 1 이상이어야 합니다.")
    @Max(value = 100, message = "Quantity는 100 이하여야 합니다.")
    private Integer quantity;

    //Controller 계층과 Service 계층 DTO 분리
    public CreateBasketDto toDto() {
        return CreateBasketDto.create(productId, quantity);
    }
}
