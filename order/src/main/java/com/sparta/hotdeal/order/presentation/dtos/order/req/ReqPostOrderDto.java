package com.sparta.hotdeal.order.presentation.dtos.order.req;

import java.util.List;
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
public class ReqPostOrderDto {
    private List<OrderedProduct> orderedProducts;
    private String addressId;
    private String orderStatus;
    private String couponId;

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderedProduct {
        private String basketId;
    }
}
