package com.sparta.hotdeal.product.application.service.promotion;

import com.sparta.hotdeal.product.domain.entity.promotion.Promotion;
import com.sparta.hotdeal.product.domain.repository.product.PromotionRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionSchedulerService {

    private final PromotionRepository promotionRepository;

    public List<Promotion> getPromotionsByStartTime(LocalDateTime currentTime) {
        return promotionRepository.findByStart(currentTime);
    }

    public List<Promotion> getPromotionsByEndTime(LocalDateTime currentTime) {
        return promotionRepository.findByEnd(currentTime);
    }
}
