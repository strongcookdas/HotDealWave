package com.sparta.hotdeal.product.application.dtos.req.product;

import com.sparta.hotdeal.product.domain.entity.product.ProductCategoryEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReqPostProductDto {

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @Positive(message = "가격은 1 이상으로 입력해주세요.")
    private Integer price;

    @Positive(message = "수량은 1 이상으로 입력해주세요.")
    private Integer quantity;

    @NotNull(message = "카테고리를 입력해주세요.")
    private ProductCategoryEnum category;

    @NotNull(message = "업체를 입력해주세요.")
    private UUID companyId;

    private String description;

    @NotNull(message = "이미지를 선택해주세요.")
    private List<MultipartFile> detailImgs;

    @NotNull(message = "이미지를 선택해주세요.")
    private MultipartFile thumbImg;
}
