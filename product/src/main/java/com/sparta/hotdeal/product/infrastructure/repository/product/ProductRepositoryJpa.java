package com.sparta.hotdeal.product.infrastructure.repository.product;

import com.sparta.hotdeal.product.domain.entity.product.Product;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryJpa extends JpaRepository<Product, UUID> {

    List<Product> findAllByIdIn(List<UUID> productIds);

    //비관락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findAllByIdInWithLock(List<UUID> productIds);
}
