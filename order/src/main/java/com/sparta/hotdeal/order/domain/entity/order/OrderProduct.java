package com.sparta.hotdeal.order.domain.entity.order;

import com.sparta.hotdeal.order.domain.entity.AuditingDate;
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
@Table(name = "p_order_product")
public class OrderProduct extends AuditingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;
    private Integer price;

    public static OrderProduct create(
            UUID orderId,
            UUID productId,
            Integer quantity,
            Integer price
    ) {
        return OrderProduct.builder()
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .build();
    }
}
