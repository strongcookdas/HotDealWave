package com.sparta.hotdeal.payment.infrastructure.repository;

import com.sparta.hotdeal.payment.domain.entity.payment.Payment;
import com.sparta.hotdeal.payment.domain.repository.PaymentRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryImpl extends JpaRepository<Payment, UUID>, PaymentRepository {
    Page<Payment> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    @Override
    default Page<Payment> findAllByUserId(UUID userId, Pageable pageable){
        return findAllByUserIdAndDeletedAtIsNull(userId, pageable);
    }
}
