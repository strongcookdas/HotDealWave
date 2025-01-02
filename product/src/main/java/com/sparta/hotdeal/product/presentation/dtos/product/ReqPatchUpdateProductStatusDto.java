package com.sparta.hotdeal.product.presentation.dtos.product;

import com.sparta.hotdeal.product.domain.entity.ProductStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPatchUpdateProductStatusDto {
    private ProductStatusEnum status;
}
