package com.sparta.hotdeal.order.infrastructure.dtos.product.req;

import com.sparta.hotdeal.order.application.dtos.order_product.OrderProductDto;
import com.sparta.hotdeal.order.domain.entity.order.Order;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPutProductQuantityDto {
    private UUID orderId;
    private List<ProductQuantityDetail> productList;

    @Getter
    @Builder
    public static class ProductQuantityDetail {
        private UUID productId;
        private int quantity;

        public static ProductQuantityDetail of(OrderProductDto orderProductDto) {
            return ProductQuantityDetail.builder()
                    .productId(orderProductDto.getProductId())
                    .quantity(orderProductDto.getQuantity())
                    .build();
        }
    }

    public static ReqPutProductQuantityDto of(Order order, List<OrderProductDto> orderProductList) {
        return ReqPutProductQuantityDto.builder()
                .orderId(order.getId())
                .productList(orderProductList.stream().map(ProductQuantityDetail::of).toList())
                .build();
    }
}