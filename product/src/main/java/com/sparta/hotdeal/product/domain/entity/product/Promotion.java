package com.sparta.hotdeal.product.domain.entity.product;

import com.sparta.hotdeal.product.application.dtos.req.product.ReqPostPromotionDto;
import com.sparta.hotdeal.product.domain.entity.AuditingDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_promotion")
@Where(clause = "deleted_at IS NULL")
public class Promotion extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private Integer discountRate;

    @Column(nullable = false)
    private Integer discountPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer remaining;

    public static Promotion create(
            ReqPostPromotionDto reqPostPromotionDto
            , Integer roundedDiscountRate) {
        return Promotion.builder()
                .productId(reqPostPromotionDto.getProductId())
                .start(reqPostPromotionDto.getStart().toLocalDateTime())
                .end(reqPostPromotionDto.getEnd().toLocalDateTime())
                .discountRate(roundedDiscountRate)
                .discountPrice(reqPostPromotionDto.getDiscountPrice())
                .quantity(reqPostPromotionDto.getQuantity())
                .remaining(reqPostPromotionDto.getQuantity())
                .build();
    }
}
