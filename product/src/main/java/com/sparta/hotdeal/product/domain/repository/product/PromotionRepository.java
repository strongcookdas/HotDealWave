package com.sparta.hotdeal.product.domain.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.Promotion;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    // productId와 start, end가 겹치는 타임세일이 있는지 확인하는 메서드
    boolean existsByProductIdAndStartBeforeAndEndAfter(UUID productId, LocalDateTime start, LocalDateTime end);
}
