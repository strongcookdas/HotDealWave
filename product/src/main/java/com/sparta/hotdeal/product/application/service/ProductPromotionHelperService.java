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

            int quantityChange = isRestore ? dto.getQuantity() : -dto.getQuantity();
            int newRemaining = promotion.getRemaining() + quantityChange;

            promotion.updateRemaining(newRemaining);
        }
    }
}

