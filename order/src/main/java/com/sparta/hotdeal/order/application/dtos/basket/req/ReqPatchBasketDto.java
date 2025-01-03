package com.sparta.hotdeal.order.application.dtos.basket.req;

import com.sparta.hotdeal.order.application.dtos.basket.UpdateBasketDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqPatchBasketDto {

    @NotNull(message = "Quantity는 필수 값입니다.")
    @Min(value = 1, message = "Quantity는 1 이상이어야 합니다.")
    @Max(value = 100, message = "Quantity는 100 이하여야 합니다.")
    private Integer quantity;

    public UpdateBasketDto toDto() {
        return UpdateBasketDto.create(quantity);
    }
}
