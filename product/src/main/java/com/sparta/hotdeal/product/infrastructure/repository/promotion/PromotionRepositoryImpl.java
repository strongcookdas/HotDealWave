package com.sparta.hotdeal.product.infrastructure.repository.promotion;

import com.sparta.hotdeal.product.domain.entity.promotion.Promotion;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
import com.sparta.hotdeal.product.domain.repository.product.PromotionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PromotionRepositoryImpl implements PromotionRepository {

    private final PromotionRepositoryJpa promotionRepositoryJpa;
    private final PromotionRepositoryCustom promotionRepositoryCustom;

    public PromotionRepositoryImpl(PromotionRepositoryJpa promotionRepositoryJpa,
                                   PromotionRepositoryCustom promotionRepositoryCustom) {
        this.promotionRepositoryJpa = promotionRepositoryJpa;
        this.promotionRepositoryCustom = promotionRepositoryCustom;
    }

    @Override
    public boolean existsByProductIdAndStartBeforeAndEndAfter(UUID productId, LocalDateTime start, LocalDateTime end) {
        return promotionRepositoryJpa.existsByProductIdAndStartBeforeAndEndAfter(productId, start, end);
    }

    @Override
    public Page<Promotion> findAllPromotions(Pageable pageable, List<UUID> productIds, PromotionStatusEnum status) {
        return promotionRepositoryCustom.findAllPromotions(pageable, productIds, status);
    }

    @Override
    public Optional<Promotion> findById(UUID id) {
        return promotionRepositoryJpa.findById(id);
    }

    @Override
    public void save(Promotion promotion) {
        promotionRepositoryJpa.save(promotion);
    }
}
