package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.req.review.ReqPostReviewDto;
import com.sparta.hotdeal.product.application.dtos.req.review.ReqPutReviewDto;
import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.res.review.ResGetReviewByIdDto;
import com.sparta.hotdeal.product.application.service.review.ReviewService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> createReview(@Valid @ModelAttribute ReqPostReviewDto reqPostReviewDto) {
        reviewService.createReview(reqPostReviewDto);
        return ResponseDto.of("리뷰가 생성되었습니다.", null);
    }

    @PutMapping("/{reviewId}")
    public ResponseDto<Void> updateReview(@Valid @PathVariable UUID reviewId,
                                          @ModelAttribute ReqPutReviewDto reqPutReviewDto) {

        // temp user name
        String username = "testUser";
        reviewService.updateReview(reviewId, reqPutReviewDto, username);
        return ResponseDto.of("리뷰가 수정되었습니다.", null);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseDto<Void> deleteReview(@PathVariable UUID reviewId) {
        // temp user name
        String username = "testUser";
        reviewService.deleteReview(reviewId, username);
        return ResponseDto.of("리뷰가 삭제되었습니다.", null);
    }

    @GetMapping("/{reviewId}")
    public ResponseDto<ResGetReviewByIdDto> getReviewById(@PathVariable UUID reviewId) {
        ResGetReviewByIdDto resGetReviewByIdDto = reviewService.getReviewById(reviewId);
        return ResponseDto.of("리뷰가 조회되었습니다.", resGetReviewByIdDto);
    }

    @GetMapping
    public ResponseDto<Page<ResGetReviewByIdDto>> getReviewList(Pageable pageable) {
        return ResponseDto.of("리뷰목록이 조회되었습니다.", reviewService.getReviewList(pageable));
    }
}