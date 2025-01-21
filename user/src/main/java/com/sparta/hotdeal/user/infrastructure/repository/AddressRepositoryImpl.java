package com.sparta.hotdeal.user.infrastructure.repository;

import com.sparta.hotdeal.user.domain.entity.Address;
import com.sparta.hotdeal.user.domain.repository.AddressRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepositoryImpl extends JpaRepository<Address, UUID>, AddressRepository {
    Page<Address> findAllByUserUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);
}
