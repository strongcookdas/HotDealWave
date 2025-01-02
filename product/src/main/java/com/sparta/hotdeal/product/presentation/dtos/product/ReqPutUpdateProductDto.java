package com.sparta.hotdeal.product.presentation.dtos.product;

import com.sparta.hotdeal.product.domain.entity.ProductCategoryEnum;
import com.sparta.hotdeal.product.domain.entity.ProductStatusEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPutUpdateProductDto {
    private String name;
    private Integer price;
    private Integer quantity;
    private ProductCategoryEnum category;
    private String description;
    private List<String> detailImgs;
    private String thumbImg;
    private ProductStatusEnum status;
}
