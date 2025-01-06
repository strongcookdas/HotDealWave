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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        productService.updateProductDiscountPrice(productDto.getProductId(), reqPostPromotionDto.getDiscountPrice());

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

        if (reqPutUpdatePromotionDto.getStart() != null || reqPutUpdatePromotionDto.getEnd() != null) {
            log.info("update Promotion check promotion is Active");
            LocalDateTime originStart = promotion.getStart();
            LocalDateTime originEnd = promotion.getEnd();

            // 타임세일이 진행중인지 확인
            checkPromotionActive(originStart, originEnd);
        }

        promotion.update(reqPutUpdatePromotionDto, discountRate);

        // 상품의 할인가격 수정
        if (reqPutUpdatePromotionDto.getDiscountPrice() != null) {
            productService.updateProductDiscountPrice(productDto.getProductId(),
                    reqPutUpdatePromotionDto.getDiscountPrice());
        }

        return ResPutPromotionDto.of(promotion.getId());
    }

    public void deletePromotion(UUID promotionId, String username) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND_EXCEPTION));

        // 타임세일이 진행중인지 확인
        checkPromotionActive(promotion.getStart(), promotion.getEnd());

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

    private void checkPromotionActive(LocalDateTime start, LocalDateTime end) {
        // 현재 시간을 시스템 기본 시간대에서 ZonedDateTime으로 구하기
        ZonedDateTime now = ZonedDateTime.now();
        log.info("now : {}", now.toString());

        // start와 end도 ZonedDateTime으로 변환
        ZonedDateTime startZoned = start.atZone(ZoneId.systemDefault());
        ZonedDateTime endZoned = end.atZone(ZoneId.systemDefault());

        log.info("start : {}", startZoned.toString());
        log.info("end : {}", endZoned.toString());

        // 현재 시간이 타임세일 기간(start ~ end) 사이에 있으면 에러를 던짐
        if (now.isAfter(startZoned) && now.isBefore(endZoned)) {
            throw new ApplicationException(ErrorCode.PROMOTION_IS_ACTIVE_EXCEPTION);
        }
    }
}
