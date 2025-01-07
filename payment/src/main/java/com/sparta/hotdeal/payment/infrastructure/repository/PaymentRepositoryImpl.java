package com.sparta.hotdeal.payment.infrastructure.repository;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryImpl extends JpaRepository<Payment, UUID>, PaymentRepository {
}
