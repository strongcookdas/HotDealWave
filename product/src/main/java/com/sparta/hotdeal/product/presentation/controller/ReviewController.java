package com.sparta.hotdeal.product.presentation.controller;

import com.sparta.hotdeal.product.application.dtos.ResponseDto;
import com.sparta.hotdeal.product.application.dtos.review.ResGetReviewByIdDto;
import com.sparta.hotdeal.product.presentation.dtos.review.ReqPostReviewDto;
import com.sparta.hotdeal.product.presentation.dtos.review.ReqPutReviewDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> createReview(@RequestBody ReqPostReviewDto reqPostReviewDto) {
        return ResponseDto.of("리뷰가 생성되었습니다.", null);
    }

    @PutMapping("/{reviewId}")
    public ResponseDto<Void> updateReview(@PathVariable int reviewId, @RequestBody ReqPutReviewDto reqPutReviewDto) {
        return ResponseDto.of("리뷰가 수정되었습니다.", null);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseDto<Void> deleteReview(@PathVariable int reviewId) {
        return ResponseDto.of("리뷰가 삭제되었습니다.", null);
    }

    @GetMapping("/{reviewId}")
    public ResponseDto<ResGetReviewByIdDto> getReviewById(@PathVariable int reviewId) {
        ResGetReviewByIdDto resGetReviewByIdDto = ResGetReviewByIdDto.builder()
                .review("리뷰텍스트 테스트")
                .rating(0.5)
                .nickname("테스트 닉네임")
                .images(List.of("img1", "img2"))
                .build();
        return ResponseDto.of("리뷰가 조회되었습니다.", resGetReviewByIdDto);
    }

    @GetMapping
    public ResponseDto<List<ResGetReviewByIdDto>> getReviewList() {
        ResGetReviewByIdDto resGetReviewByIdDto = ResGetReviewByIdDto.builder()
                .review("리뷰텍스트 테스트")
                .rating(0.5)
                .nickname("테스트 닉네임")
                .images(List.of("img1", "img2"))
                .build();

        List<ResGetReviewByIdDto> responseDtos = new ArrayList<>();
        responseDtos.add(resGetReviewByIdDto);
        return ResponseDto.of("리뷰목록이 조회되었습니다.", responseDtos);
    }
}
