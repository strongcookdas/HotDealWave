package com.sparta.hotdeal.product.application.dtos.req.review;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ReqPutReviewDto {
    @NotNull(message = "평점을 입력해주세요.")
    private double rating;
    @NotNull(message = "내용을 입력해주세요.")
    private String review;
    @NotNull(message = "사진을 등록해주세요.")
    private List<MultipartFile> reviewImgs;
}