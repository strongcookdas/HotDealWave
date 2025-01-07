package com.sparta.hotdeal.product.infrastructure.repository.promotion;

import com.sparta.hotdeal.product.domain.entity.promotion.Promotion;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionRepositoryCustom {

    Page<Promotion> findAllPromotions(Pageable pageable, List<UUID> productIds, PromotionStatusEnum status);
}
