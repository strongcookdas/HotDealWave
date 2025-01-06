package com.sparta.hotdeal.user.domain.repository;

import com.sparta.hotdeal.user.domain.entity.Address;

public interface AddressRepository {
    Address save(Address address);
}
