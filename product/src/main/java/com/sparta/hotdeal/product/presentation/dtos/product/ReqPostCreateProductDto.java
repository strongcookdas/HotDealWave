package com.sparta.hotdeal.product.presentation.dtos.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductCategoryEnum;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPostCreateProductDto {
    private String name;
    private Integer price;
    private Integer quantity;
    private ProductCategoryEnum category;
    private UUID companyId;
    private String description;
    private List<String> detailImgs;
    private String thumbImg;
}
