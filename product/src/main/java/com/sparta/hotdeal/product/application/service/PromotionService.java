package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostPromotionDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResGetProductDto;
import com.sparta.hotdeal.product.application.dtos.res.product.ResPostPromotionDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.domain.entity.product.Promotion;
import com.sparta.hotdeal.product.domain.repository.product.PromotionRepository;
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
        // 상품 가격보다 작은지
        if (reqPostPromotionDto.getDiscountPrice() >= productDto.getPrice()) {
            throw new ApplicationException(ErrorCode.PROMOTION_INVALID_PRICE_EXCEPTION);
        }

        // 상품 수량보다 적은지
        if (reqPostPromotionDto.getQuantity() > productDto.getQuantity()) {
            throw new ApplicationException(ErrorCode.PROMOTION_INVALID_QUANTITY_EXCEPTION);
        }

        // 할인율 계산
        int discountRate = calculateDiscountRate(productDto.getPrice(), reqPostPromotionDto.getDiscountPrice());

        Promotion promotion = Promotion.create(reqPostPromotionDto, discountRate);

        promotionRepository.save(promotion);

        return ResPostPromotionDto.of(promotion.getId());
    }

    public int calculateDiscountRate(int originPrice, int discountPrice) {
        double discountRate =
                ((double) (originPrice - discountPrice) / originPrice)
                        * 100;

        return (int) Math.round(discountRate);
    }
}
