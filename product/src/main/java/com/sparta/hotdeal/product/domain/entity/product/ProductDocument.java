package com.sparta.hotdeal.product.domain.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드 무시
public class ProductDocument {
    @Id
    private String id;
    private String name;
    private String description;
    private Integer price;
    private int quantity;
    private ProductCategoryEnum category;
    private String companyId;
    private ProductStatusEnum status;
    private double ratingSum;
    private int reviewCnt;
    private Integer discountPrice;
    private List<String> detailImgs;
    private String thumbImg;
    private String createdAt;
}