package com.sparta.hotdeal.product.presentation.dtos.review;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostReviewDto {
    private UUID orderId;
    private UUID productId;
    private Long userId;
    private double rating;
    private String review;
    private List<String> images;
}
