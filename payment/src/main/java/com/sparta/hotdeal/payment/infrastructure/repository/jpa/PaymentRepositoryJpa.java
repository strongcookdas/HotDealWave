package com.sparta.hotdeal.payment.infrastructure.repository.jpa;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaymentRepositoryJpa extends JpaRepository<Payment, UUID> {
    Page<Payment> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    Optional<Payment> findByTidAndDeletedAtIsNull(String tid);

    Optional<Payment> findByIdAndUserId(UUID paymentId, UUID userId);

    Optional<Payment> findByOrderIdAndUserId(UUID orderId, UUID userId);
}
