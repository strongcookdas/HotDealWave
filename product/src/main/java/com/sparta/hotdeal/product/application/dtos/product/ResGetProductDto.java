package com.sparta.hotdeal.product.application.dtos.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductCategoryEnum;
import com.sparta.hotdeal.product.domain.entity.product.ProductStatusEnum;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResGetProductDto {
    private UUID productId;
    private String name;
    private Integer price;
    private Integer quantity;
    private ProductCategoryEnum category;
    private UUID companyId;
    private String description;
    private List<String> detailImgs;
    private String thumbImg;
    private ProductStatusEnum status;
    private double rating;
    private Integer reviewCnt;
    private Integer discountPrice;
}
