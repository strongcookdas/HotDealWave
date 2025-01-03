package com.sparta.hotdeal.product.application.dtos.review;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetReviewByIdDto {
    private String nickname;
    private double rating;
    private String review;
    private List<String> images;
}
