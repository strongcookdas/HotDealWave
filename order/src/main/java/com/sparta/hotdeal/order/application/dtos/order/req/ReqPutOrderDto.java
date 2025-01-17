package com.sparta.hotdeal.order.application.dtos.order.req;

import com.sparta.hotdeal.order.domain.entity.order.OrderStatus;
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
public class ReqPutOrderDto {
    private OrderStatus orderStatus;

    public static ReqPutOrderDto of(OrderStatus orderStatus) {
        return ReqPutOrderDto.builder()
                .orderStatus(orderStatus)
                .build();
    }
}
