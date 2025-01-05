package com.sparta.hotdeal.order.infrastructure.dtos.product;

import com.sparta.hotdeal.order.application.dtos.product.ProductListDto;
import com.sparta.hotdeal.order.application.dtos.product.res.ResGetProductListForOrderDto;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ResGetProductListDto {
    private UUID productId;
    private String name;
    private Integer price;
    private Integer quantity;
    private String category;
    private UUID companyId;
    private String description;
    private List<String> detailImgs;
    private String thumbImg;
    private String status;
    private double rating;
    private Integer reviewCnt;
    private Integer discountPrice;

    public ProductListDto toDto() {
        return ProductListDto.create(
                productId,
                name,
                price,
                category,
                thumbImg,
                discountPrice,
                status,
                rating,
                reviewCnt,
                quantity
        );
    }

    public ResGetProductListForOrderDto toGetProductListForOrderDto() {
        return ResGetProductListForOrderDto.create(
                productId,
                name,
                price,
                category,
                thumbImg,
                discountPrice,
                status,
                rating,
                reviewCnt,
                quantity,
                companyId
        );
    }
}
