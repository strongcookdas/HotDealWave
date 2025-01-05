package com.sparta.hotdeal.order.application.dtos.order.req;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqPostOrderDto {
    private List<UUID> basketList;
    private UUID addressId;
    private UUID couponId;
}
