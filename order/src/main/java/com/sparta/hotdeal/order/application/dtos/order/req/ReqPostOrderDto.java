package com.sparta.hotdeal.order.application.dtos.order.req;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ReqPostOrderDto {
    @NotNull(message = "장바구니 ID는 필수 값입니다.")
    private List<UUID> basketList;
    @NotNull(message = "주소 ID는 필수 값입니다.")
    private UUID addressId;
    private UUID couponId;
}
