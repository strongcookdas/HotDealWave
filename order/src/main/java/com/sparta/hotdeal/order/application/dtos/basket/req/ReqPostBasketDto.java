package com.sparta.hotdeal.order.application.dtos.basket.req;

import com.sparta.hotdeal.order.application.dtos.basket.CreateBasketDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReqPostBasketDto {
    private UUID productId;
    private Integer quantity;

    //Controller 계층과 Service 계층 DTO 분리
    public CreateBasketDto toDto() {
        return CreateBasketDto.create(productId, quantity);
    }
}
