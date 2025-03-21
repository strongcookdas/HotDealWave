package com.sparta.hotdeal.order.domain.entity.order;

import com.sparta.hotdeal.order.domain.entity.AuditingDate;
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
@Table(name = "p_order")
public class Order extends AuditingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID addressId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private String name;

    private UUID couponId;

    private Integer couponDiscountAmount;


    public static Order create(
            UUID addressId,
            UUID userId,
            Integer totalAmount,
            String name,
            UUID couponId,
            Integer couponDiscountAmount
    ) {
        return Order.builder()
                .addressId(addressId)
                .userId(userId)
                .totalAmount(totalAmount)
                .name(name)
                .status(OrderStatus.CREATE)
                .couponId(couponId)
                .couponDiscountAmount(couponDiscountAmount)
                .build();
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
