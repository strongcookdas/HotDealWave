package com.sparta.hotdeal.product.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hotdeal.product.domain.entity.product.Product;
import com.sparta.hotdeal.product.domain.entity.product.QProduct;
import com.sparta.hotdeal.product.domain.repository.product.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Product> findAllWithSearchAndPaging(String search, Pageable pageable) {
        QProduct qProduct = QProduct.product;

        // 검색 조건
        BooleanBuilder predicate = new BooleanBuilder();
        if (search != null && !search.isEmpty()) {
            predicate.and(qProduct.name.containsIgnoreCase(search))
                    .or(qProduct.description.containsIgnoreCase(search));
        }

        // 동적 정렬 조건
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, qProduct);

        // 데이터 조회
        List<Product> products = queryFactory
                .selectFrom(qProduct)
                .where(predicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(products, pageable, total);
    }

    // Pageable의 Sort를 QueryDSL의 OrderSpecifier로 변환
    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QProduct qProduct) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        Sort sort = pageable.getSort();

        for (Sort.Order order : sort) {
            OrderSpecifier<?> orderSpecifier = switch (order.getProperty()) {
                case "createdAt" -> order.isAscending()
                        ? qProduct.createdAt.asc()
                        : qProduct.createdAt.desc();
                case "updatedAt" -> order.isAscending()
                        ? qProduct.updatedAt.asc()
                        : qProduct.updatedAt.desc();
                case "price" -> order.isAscending()
                        ? qProduct.price.asc()
                        : qProduct.price.desc();
                default -> throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
            };

            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }

}
