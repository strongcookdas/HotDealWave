package com.sparta.hotdeal.product.presentation.dtos.review;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPutReviewDto {
    private double rating;
    private String review;
    private List<String> images;
}
