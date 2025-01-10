package com.sparta.hotdeal.order.infrastructure.dtos.coupon.req;

import com.sparta.hotdeal.order.application.dtos.product.ProductDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqPostCouponValidateDto {
    private List<Product> products;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        private UUID companyId;
        private int price;

        public static Product create(ProductDto productDto) {
            return Product.builder()
                    .companyId(productDto.getCompanyId())
                    .price((productDto.getDiscountPrice() == null) ? productDto.getPrice()
                            : productDto.getDiscountPrice())
                    .build();
        }
    }

    public static ReqPostCouponValidateDto create(List<Product> productList) {
        return ReqPostCouponValidateDto.builder()
                .products(productList)
                .build();
    }
}

