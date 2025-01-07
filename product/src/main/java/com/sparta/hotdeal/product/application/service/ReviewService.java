package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.review.ReqPostReviewDto;
import com.sparta.hotdeal.product.application.dtos.req.review.ReqPutReviewDto;
import com.sparta.hotdeal.product.application.dtos.res.review.ResGetReviewByIdDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.application.service.client.OrderClientService;
import com.sparta.hotdeal.product.domain.entity.product.File;
import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.entity.product.SubFile;
import com.sparta.hotdeal.product.domain.entity.review.Review;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.domain.repository.review.ReviewRepository;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetOrderByIdDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderClientService orderClientService;
    private final FileService fileService;
    private final SubFileService subFileService;

    public void createReview(ReqPostReviewDto reqPostReviewDto) {
        // (1) 유효한 주문 확인
        ResGetOrderByIdDto fetchedOrder = orderClientService.getOrder(reqPostReviewDto.getOrderId());

        // (2) 유효한 상품 확인
        productRepository.findById(reqPostReviewDto.getProductId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        File reviewImgs = fileService.saveFile();
        for (MultipartFile file : reqPostReviewDto.getReviewImgs()) {
            subFileService.saveImg(file, reviewImgs);
        }

        Review review = Review.create(reqPostReviewDto.getOrderId(), reqPostReviewDto.getProductId(),
                reqPostReviewDto.getUserId(), reqPostReviewDto.getRating(), reqPostReviewDto.getReview(), reviewImgs);

        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(UUID reviewId, ReqPutReviewDto reqPutReviewDto, String username) {
        // (1) 리뷰 존재 유무 확인
        Review fetchedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // (2) 상품 존재 유무 확인
        Product fetchedProduct = productRepository.findById(fetchedReview.getProductID())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        fetchedProduct.decrementReview(fetchedReview.getRating());
        fetchedProduct.incrementReview(reqPutReviewDto.getRating());

        File reviewImgs = fetchedReview.getReviewImgs();
        if (reqPutReviewDto.getReviewImgs() != null) {
            subFileService.updateSubFiles(reqPutReviewDto.getReviewImgs(), reviewImgs, username);
        }

        fetchedReview.update(reqPutReviewDto.getRating(), reqPutReviewDto.getReview());
    }

    public void deleteReview(UUID reviewId, String username) {
        // (1) 리뷰 존재 유무 확인
        Review fetchedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // (2) 상품 존재 유무 확인
        Product fetchedProduct = productRepository.findById(fetchedReview.getProductID())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        fetchedProduct.decrementReview(fetchedReview.getRating());

        File reviewImgs = fetchedReview.getReviewImgs();
        fileService.deleteFile(reviewImgs, username);
        subFileService.deleteImg(reviewImgs, username);

        fetchedReview.delete(username);
    }

    @Transactional(readOnly = true)
    public ResGetReviewByIdDto getReviewById(UUID reviewId) {
        // (1) 리뷰 존재 유무 확인
        Review fetchedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        return ResGetReviewByIdDto.create(fetchedReview);
    }

    @Transactional(readOnly = true)
    public Page<ResGetReviewByIdDto> getReviewList(Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAll(pageable);
        return reviewPage.map(ResGetReviewByIdDto::create);
    }
}
