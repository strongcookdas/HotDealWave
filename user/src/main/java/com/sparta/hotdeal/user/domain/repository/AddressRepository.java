package com.sparta.hotdeal.user.domain.repository;

import com.sparta.hotdeal.user.domain.entity.Address;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(UUID addressId);
    Page<Address> findAllByUserUserId(UUID userId, Pageable pageable);
}
