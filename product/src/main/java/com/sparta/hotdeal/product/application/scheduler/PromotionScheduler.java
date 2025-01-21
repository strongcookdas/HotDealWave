package com.sparta.hotdeal.product.application.scheduler;

import com.sparta.hotdeal.product.application.service.product.ProductService;
import com.sparta.hotdeal.product.application.service.promotion.PromotionSchedulerService;
import com.sparta.hotdeal.product.application.service.promotion.PromotionService;
import com.sparta.hotdeal.product.domain.entity.promotion.Promotion;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PromotionScheduler {

    private final PromotionSchedulerService promotionSchedulerService;
    private final PromotionService promotionService;
    private final ProductService productService;

    @Scheduled(cron = "0 0 * * * ?") // 매 정각 실행
    public void changePromotionStatusToInProgress() {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Scheduler : Change promotion status to in progress : {}", currentTime);
        List<Promotion> promotions = promotionSchedulerService.getPromotionsByStartTime(
                currentTime.truncatedTo(ChronoUnit.SECONDS));

        for (Promotion promotion : promotions) {
            log.info("Starting promotion with ID: {}", promotion.getId());

            // Promotion 상태 변경
            promotionService.updatePromotionStatus(promotion, PromotionStatusEnum.IN_PROGRESS);

            // 관련된 Product의 discountPrice 업데이트
            productService.updateProductDiscountPrice(promotion.getProductId(), promotion.getDiscountPrice());
        }
    }

    @Scheduled(cron = "0 0 * * * ?") // 매 정각 실행
    public void changePromotionStatusToCompleted() {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Scheduler : Change promotion status to completed : {}", currentTime);
        List<Promotion> promotions = promotionSchedulerService.getPromotionsByEndTime(
                currentTime.truncatedTo(ChronoUnit.SECONDS));

        for (Promotion promotion : promotions) {
            log.info("Ending promotion with ID: {}", promotion.getId());

            // Promotion 상태 변경
            promotionService.updatePromotionStatus(promotion, PromotionStatusEnum.COMPLETED);

            // 관련된 Product의 discountPrice null로 설정
            productService.updateProductDiscountPrice(promotion.getProductId(), null);
        }
    }
}
