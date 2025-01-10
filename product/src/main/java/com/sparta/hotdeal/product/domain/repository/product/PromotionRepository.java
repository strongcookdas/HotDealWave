package com.sparta.hotdeal.product.domain.repository.product;

import com.sparta.hotdeal.product.domain.entity.promotion.Promotion;
import com.sparta.hotdeal.product.domain.entity.promotion.PromotionStatusEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionRepository {

    // productId와 start, end가 겹치는 타임세일이 있는지 확인하는 메서드
    boolean existsByProductIdAndStartBeforeAndEndAfter(UUID productId, LocalDateTime start, LocalDateTime end);

    Page<Promotion> findAllPromotions(Pageable pageable, List<UUID> productIds, PromotionStatusEnum status);

    Optional<Promotion> findById(UUID id);

    void save(Promotion promotion);

    Optional<Promotion> findByProductIdAndStatus(UUID productId, PromotionStatusEnum status);

    // 시작 시간이 일치하는 Promotion 리스트 조회
    List<Promotion> findByStart(LocalDateTime startTime);

    // 종료 시간이 일치하는 Promotion 리스트 조회
    List<Promotion> findByEnd(LocalDateTime endTime);
}
