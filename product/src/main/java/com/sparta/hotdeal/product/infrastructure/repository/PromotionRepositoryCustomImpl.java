package com.sparta.hotdeal.product.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hotdeal.product.application.exception.ApplicationException;
import com.sparta.hotdeal.product.application.exception.ErrorCode;
import com.sparta.hotdeal.product.domain.entity.product.Promotion;
import com.sparta.hotdeal.product.domain.entity.product.PromotionStatusEnum;
import com.sparta.hotdeal.product.domain.entity.product.QPromotion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PromotionRepositoryCustomImpl implements PromotionRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPromotion promotion = QPromotion.promotion;

    @PersistenceContext
    private EntityManager entityManager;

    public PromotionRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<Promotion> findAllPromotions(Pageable pageable, List<UUID> productIds, PromotionStatusEnum status) {
        BooleanBuilder predicate = new BooleanBuilder();

        // 필터링 조건 추가
        if (productIds != null && !productIds.isEmpty()) {
            predicate.and(promotion.productId.in(productIds));
        }
        if (status != null) {
            predicate.and(promotion.status.eq(status));
        }

        // 쿼리 실행
        List<Promotion> promotions = queryFactory
                .selectFrom(promotion)
                .where(predicate)
                .orderBy(createOrderSpecifier(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .selectFrom(promotion)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(promotions, pageable, total);
    }

    private OrderSpecifier<?> createOrderSpecifier(Pageable pageable) {
        String sortBy = pageable.getSort().iterator().next().getProperty();
        Boolean direction = pageable.getSort().iterator().next().isAscending();

        OrderSpecifier<?> orderSpecifier = switch (sortBy) {
            case "createdAt" -> direction ? promotion.createdAt.asc() : promotion.createdAt.desc();
            case "updatedAt" -> direction ? promotion.updatedAt.asc() : promotion.updatedAt.desc();
            default -> throw new ApplicationException(ErrorCode.INVALID_SORT_EXCEPTION);
        };

        return orderSpecifier;
    }

}
