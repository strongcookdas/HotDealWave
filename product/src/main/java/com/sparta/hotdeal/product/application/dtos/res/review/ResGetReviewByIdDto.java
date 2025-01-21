package com.sparta.hotdeal.product.application.dtos.res.review;

import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import com.sparta.hotdeal.product.domain.entity.review.Review;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetReviewByIdDto {
    private String nickname;
    private double rating;
    private String review;
    private List<String> reviewImgs;

    public static ResGetReviewByIdDto create(Review review) {
        File reviewImgsFile = review.getReviewImgs();
        List<String> reviewImgs = reviewImgsFile.getSubFiles().stream().map(SubFile::getResource).toList();

        return ResGetReviewByIdDto.builder()
                .nickname(review.getNickname())
                .rating(review.getRating())
                .review(review.getReview())
                .reviewImgs(reviewImgs)
                .build();
    }
}