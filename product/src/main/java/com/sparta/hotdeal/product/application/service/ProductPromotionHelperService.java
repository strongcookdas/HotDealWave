package com.sparta.hotdeal.product.application.service;

import com.sparta.hotdeal.product.application.dtos.req.promotion.ReqPromotionQuantityDto;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.domain.entity.promotion.Promotion;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepository;
import com.sparta.hotdeal.product.domain.repository.product.PromotionRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductPromotionHelperService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductPromotionHelperService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public void processPromotionQuantity(List<ReqPromotionQuantityDto> reqPromotionQuantityDtos, boolean isRestore) {
        for (ReqPromotionQuantityDto dto : reqPromotionQuantityDtos) {
            Promotion promotion = promotionRepository
                    .findByProductIdAndStatus(dto.getProductId(), PromotionStatusEnum.IN_PROGRESS)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND_EXCEPTION));
            log.info("Processing promotion: {}", promotion.getProductId());

            // 할인 상품의 남은 수량 확인
            if (!isRestore && promotion.getRemaining() < dto.getQuantity()) {
                log.warn("할인 상품 재고 부족 상품 ID: {}, 요청 수량: {}, 할인 상품 남은 수량: {}",
                        dto.getProductId(), dto.getQuantity(), promotion.getRemaining());
                throw new ApplicationException(ErrorCode.INSUFFICIENT_PROMOTION_QUANTITY_EXCEPTION);
            }

            if (isRestore) {
                promotion.increaseRemaining(dto.getQuantity());
            } else {
                promotion.decreaseRemaining(dto.getQuantity());
            }
        }
    }
}

