package com.sparta.hotdeal.user.domain.repository;

import com.sparta.hotdeal.user.domain.entity.Address;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(UUID addressId);
}
