package com.sparta.hotdeal.coupon.domain.entity;

import com.sparta.hotdeal.coupon.application.exception.CustomException;
import com.sparta.hotdeal.coupon.application.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "p_coupon_info")
public class CouponInfo extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coupon_info_id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "issued_count", nullable = false)
    private int issuedCount;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

    @Column(name = "min_order_amount", nullable = false)
    private int minOrderAmount;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CouponStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type", nullable = false)
    private CouponType couponType;

    @Column(name = "company_id")
    private UUID companyId;

//    @Version
//    private Integer version; // 낙관적 락을 위한 버전 필드

    public void updateStatus(CouponStatus status) {
        this.status = status;
    }

    public void update(String name, int quantity, int discountAmount, int minOrderAmount, LocalDate expirationDate, CouponType couponType, UUID companyId) {
        this.name = name;
        this.quantity = quantity;
        this.discountAmount = discountAmount;
        this.minOrderAmount = minOrderAmount;
        this.expirationDate = expirationDate;
        this.couponType = couponType;
        this.companyId = companyId;
    }

    public void incrementIssuedCount() {
        if (this.issuedCount >= this.quantity) {
            throw new CustomException(ErrorCode.COUPON_ISSUED_LIMIT_REACHED);
        }

        this.issuedCount++;

        // 최대치 도달 시 상태 변경
        if (this.issuedCount == this.quantity) {
            this.updateStatus(CouponStatus.OUT_OF_STOCK);
        }
    }

}
