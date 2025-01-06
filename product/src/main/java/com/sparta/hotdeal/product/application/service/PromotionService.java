package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.req.product.ReqPutPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPutPromotionDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.domain.entity.product.Promotion;
import com.sparta.hotdeal.product.domain.repository.product.PromotionRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductService productService;


    public ResPostPromotionDto createPromotion(ReqPostPromotionDto reqPostPromotionDto) throws ApplicationException {
        // 상품 조회
        ResGetProductDto productDto = productService.getProduct(reqPostPromotionDto.getProductId());

        // 입력 값 검증
        if (promotionRepository.existsByProductIdAndStartBeforeAndEndAfter(
                reqPostPromotionDto.getProductId(),
                reqPostPromotionDto.getEnd(),
                reqPostPromotionDto.getStart())) {
            throw new ApplicationException(ErrorCode.PROMOTION_DUPLICATE_EXCEPTION);
        }

        // 상품 가격보다 작은지
        validateDiscountPrice(productDto.getPrice(), reqPostPromotionDto.getDiscountPrice());

        // 상품 수량보다 적은지
        validateDiscountQuantity(productDto.getQuantity(), reqPostPromotionDto.getQuantity());

        // 할인율 계산
        int discountRate = calculateDiscountRate(productDto.getPrice(), reqPostPromotionDto.getDiscountPrice());

        Promotion promotion = Promotion.create(reqPostPromotionDto, discountRate);

        promotionRepository.save(promotion);

        // 상품의 할인가격 수정

        return ResPostPromotionDto.of(promotion.getId());
    }

    public ResPutPromotionDto updatePromotion(UUID promotionId, ReqPutPromotionDto reqPutUpdatePromotionDto) {
        // 타임세일 조회
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND_EXCEPTION));

        // 상품 조회
        UUID productId = reqPutUpdatePromotionDto.getProductId() == null ? promotion.getProductId()
                : reqPutUpdatePromotionDto.getProductId();
        ResGetProductDto productDto = productService.getProduct(productId);

        if (promotionRepository.existsByProductIdAndStartBeforeAndEndAfter(
                productDto.getProductId(),
                reqPutUpdatePromotionDto.getEnd(),
                reqPutUpdatePromotionDto.getStart())) {
            throw new ApplicationException(ErrorCode.PROMOTION_DUPLICATE_EXCEPTION);
        }

        int discountRate = promotion.getDiscountRate();
        if (reqPutUpdatePromotionDto.getDiscountPrice() != null) {
            validateDiscountPrice(productDto.getPrice(), reqPutUpdatePromotionDto.getDiscountPrice());
            discountRate = calculateDiscountRate(productDto.getPrice(), reqPutUpdatePromotionDto.getDiscountPrice());
        }

        if (reqPutUpdatePromotionDto.getQuantity() != null) {
            validateDiscountQuantity(productDto.getQuantity(), reqPutUpdatePromotionDto.getQuantity());
        }

        promotion.update(reqPutUpdatePromotionDto, discountRate);

        // 상품의 할인가격 수정

        return ResPutPromotionDto.of(promotion.getId());
    }

    public void deletePromtoion(UUID promotionId, String username) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND_EXCEPTION));

        // 진행중인 타임세일인지 확인
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(promotion.getStart()) && now.isBefore(promotion.getEnd())) {
            throw new ApplicationException(ErrorCode.PROMOTION_IS_ACTIVE_EXCEPTION);
        }

        // 타임세일 삭제 처리
        promotion.delete(username);
    }

    public int calculateDiscountRate(int originPrice, int discountPrice) {
        double discountRate =
                ((double) (originPrice - discountPrice) / originPrice)
                        * 100;

        return (int) Math.round(discountRate);
    }

    public void validateDiscountPrice(int originPrice, int discountPrice) {
        if (discountPrice >= originPrice) {
            throw new ApplicationException(ErrorCode.PROMOTION_INVALID_PRICE_EXCEPTION);
        }
    }

    public void validateDiscountQuantity(int originQuantity, int discountQuantity) {
        if (discountQuantity > originQuantity) {
            throw new ApplicationException(ErrorCode.PROMOTION_INVALID_QUANTITY_EXCEPTION);
        }
    }
}
