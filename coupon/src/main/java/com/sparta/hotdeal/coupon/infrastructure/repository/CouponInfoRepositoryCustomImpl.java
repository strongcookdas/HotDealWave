package com.sparta.hotdeal.coupon.infrastructure.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import com.sparta.hotdeal.coupon.domain.entity.CouponInfo;
import com.sparta.hotdeal.coupon.domain.entity.CouponStatus;
import com.sparta.hotdeal.coupon.domain.entity.QCouponInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.hotdeal.coupon.domain.entity.QCouponInfo.couponInfo;

@Repository
@RequiredArgsConstructor
public class CouponInfoRepositoryCustomImpl implements CouponInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<CouponInfo> findCouponInfoWithSearchAndPaging(Pageable pageable, String search, CouponStatus couponStatus) {
        QCouponInfo couponInfo = QCouponInfo.couponInfo;

        BooleanExpression booleanExpression = couponInfo.deletedAt.isNull()
                .and(buildSearchExpression(search))
                .and(buildStatusExpression(couponStatus));


        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageable, couponInfo);

        List<CouponInfo> results = queryFactory.selectFrom(couponInfo)
                .where(booleanExpression)
                .offset(pageable.getOffset())
                .orderBy(orderSpecifier)
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = getTotalCount(couponInfo, booleanExpression);

        return new PageImpl<>(results, pageable, totalCount);
    }

    // 이름 기반 검색
    private BooleanExpression buildSearchExpression(String search) {
        if (search == null) {
            return null;
        }
        return couponInfo.name.containsIgnoreCase(search);
    }

    private BooleanExpression buildStatusExpression(CouponStatus couponStatus) {
        if (couponStatus == null) {
            return null;
        }
        return couponInfo.status.eq(couponStatus);
    }

    private long getTotalCount(QCouponInfo couponInfo, BooleanExpression booleanExpression) {
        return queryFactory.selectFrom(couponInfo)
                .where(booleanExpression)
                .fetch().size();
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable, QCouponInfo couponInfo) {
        Sort sort = pageable.getSort();

        if (sort.toList().size() != 1) {
            throw new CustomException(ErrorCode.INVALID_SORT_EXCEPTION);
        }

        Sort.Order order = sort.iterator().next();
        return switch (order.getProperty()) {
            case "createdAt" -> order.isAscending()
                    ? couponInfo.createdAt.asc()
                    : couponInfo.createdAt.desc();
            case "updatedAt" -> order.isAscending()
                    ? couponInfo.updatedAt.asc()
                    : couponInfo.updatedAt.desc();
            case "discountAmount" -> order.isAscending()
                    ? couponInfo.discountAmount.asc()
                    : couponInfo.discountAmount.desc();
            default -> throw new CustomException(ErrorCode.INVALID_SORT_EXCEPTION);
        };
    }

}
