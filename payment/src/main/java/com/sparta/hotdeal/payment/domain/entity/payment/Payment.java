package com.sparta.hotdeal.payment.domain.entity.payment;

import com.sparta.hotdeal.payment.domain.entity.AuditingDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_payment")
public class Payment extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer refundAmount;

    @Column(nullable = false)
    private String tid;

    public static Payment create(
            UUID orderId,
            UUID userId,
            PaymentStatus status,
            Integer amount,
            Integer refundAmount,
            String tid
    ) {
        return Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .status(status)
                .amount(amount)
                .refundAmount(refundAmount)
                .tid(tid)
                .build();
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public void updateRefundInfo(Integer refundAmount){
        this.refundAmount = refundAmount;
    }
}
