package com.sparta.hotdeal.payment.domain.repository;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface PaymentRepository {
    Optional<Payment> findByTid(String tid);

    Payment save(Payment payment);

    Optional<Payment> findByIdAndUserId(UUID paymentId, UUID userId);

    Page<Payment> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Payment> findByOrderIdAndUserId(UUID orderId, UUID userId);

    Payment saveAndFlush(Payment payment);
}
