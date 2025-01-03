package com.sparta.hotdeal.order.application.dtos.basket.req;

import com.sparta.hotdeal.order.application.dtos.basket.UpdateBasketDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqPatchBasketDto {
    private Integer quantity;

    public UpdateBasketDto toDto() {
        return UpdateBasketDto.create(quantity);
    }
}
