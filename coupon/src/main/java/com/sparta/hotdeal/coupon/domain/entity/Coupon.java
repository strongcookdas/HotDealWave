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
@Table(name = "p_coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coupon_id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_info_id", nullable = false)
    private CouponInfo couponInfo;

    @Column(name = "daily_issued_date")
    private LocalDate dailyIssuedDate;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "used_date")
    private LocalDate usedDate;
}
