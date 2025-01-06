package com.sparta.hotdeal.user.infrastructure.repository;

import com.sparta.hotdeal.user.domain.entity.Address;
import com.sparta.hotdeal.user.domain.repository.AddressRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepositoryImpl extends JpaRepository<Address, UUID>, AddressRepository {
}
