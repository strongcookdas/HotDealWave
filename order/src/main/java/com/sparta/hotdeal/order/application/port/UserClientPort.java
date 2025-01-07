package com.sparta.hotdeal.order.application.port;

import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.user.UserDto;
import java.util.UUID;

public interface UserClientPort {
    AddressDto getAddress(UUID addressId);
    UserDto getUserById(UUID userId);
}
