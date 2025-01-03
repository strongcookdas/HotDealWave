package com.sparta.hotdeal.product.application.dtos.req.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductCategoryEnum;
import com.sparta.hotdeal.product.domain.entity.product.ProductStatusEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPutProductDto {
    private String name;
    private Integer price;
    private Integer quantity;
    private ProductCategoryEnum category;
    private String description;
    private List<MultipartFile> detailImgs;
    private MultipartFile thumbImg;
    private ProductStatusEnum status;
}
