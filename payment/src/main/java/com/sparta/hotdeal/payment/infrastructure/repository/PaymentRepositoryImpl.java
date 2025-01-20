package com.sparta.hotdeal.payment.infrastructure.repository;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import com.sparta.hotdeal.payment.infrastructure.repository.jpa.PaymentRepositoryJpa;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentRepositoryJpa paymentRepositoryJpa;

    @Override
    public Optional<Payment> findByTid(String tid) {
        return paymentRepositoryJpa.findByTidAndDeletedAtIsNull(tid);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepositoryJpa.save(payment);
    }

    @Override
    public Optional<Payment> findByIdAndUserId(UUID paymentId, UUID userId) {
        return paymentRepositoryJpa.findByIdAndUserId(paymentId, userId);
    }

    @Override
    public Page<Payment> findAllByUserId(UUID userId, Pageable pageable) {
        return paymentRepositoryJpa.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
    }

    @Override
    public Optional<Payment> findByOrderIdAndUserId(UUID orderId, UUID userId) {
        return paymentRepositoryJpa.findByOrderIdAndUserId(orderId, userId);
    }
}
