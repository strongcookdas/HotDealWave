package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.user.UserDto;
import java.util.UUID;

public interface UserClientPort {
    AddressDto getAddress(UUID userId, String email, String role, UUID addressId);

    UserDto getUserById(UUID userId, String email, String role);
}
