package com.sparta.hotdeal.payment.domain.repository;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {
    Optional<Payment> findByTid(String tid);
}
