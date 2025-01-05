package com.sparta.hotdeal.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_coupon_info")
public class CouponInfo {

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
}
