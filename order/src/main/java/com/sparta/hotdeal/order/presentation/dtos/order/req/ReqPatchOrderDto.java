package com.sparta.hotdeal.order.presentation.dtos.order.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReqPatchOrderDto {
    private String orderStatus;
}
