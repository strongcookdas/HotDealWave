package com.sparta.hotdeal.order.domain.entity.basket;

import com.sparta.hotdeal.order.domain.entity.AuditingDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "p_basket")
public class Basket extends AuditingDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Integer quantity;

    public static Basket create(
            UUID productId,
            UUID userId,
            Integer quantity
    ) {
        return Basket.builder()
                .productId(productId)
                .userId(userId)
                .quantity(quantity)
                .build();
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void delete(String email){
        remove(email);
    }
}
