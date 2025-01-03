package com.sparta.hotdeal.product.application.dtos.req.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductCategoryEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReqPostProductDto {

    @NotBlank
    private String name;

    @Positive
    private Integer price;

    @Positive
    private Integer quantity;

    @NotBlank
    private ProductCategoryEnum category;

    @NotBlank
    private UUID companyId;

    private String description;
    private List<MultipartFile> detailImgs;
    private MultipartFile thumbImg;
}
