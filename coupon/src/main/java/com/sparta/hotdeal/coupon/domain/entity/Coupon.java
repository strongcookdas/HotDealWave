package com.sparta.hotdeal.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "p_coupon")
public class Coupon extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "coupon_id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_info_id", nullable = false)
    private CouponInfo couponInfo;

    @Column(name = "daily_issued_date") // 데일리 쿠폰 발급 여부를 위한 필드, 선착순 쿠폰의 경우는 null
    private LocalDate dailyIssuedDate;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "used_date")
    private LocalDate usedDate;

    public void useCoupon() {
        this.isUsed = true;
        this.usedDate = LocalDate.now();
    }

    public void recoverCoupon() {
        this.isUsed = false;
        this.usedDate = null;
    }
}
