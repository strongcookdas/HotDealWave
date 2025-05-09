package com.sparta.hotdeal.product.application.dtos.req.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPatchProductStatusDto {

    @NotNull(message = "상태를 입력해주세요.")
    private ProductStatusEnum status;
}
