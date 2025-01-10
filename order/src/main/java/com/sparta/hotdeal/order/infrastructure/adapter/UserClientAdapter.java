package com.sparta.hotdeal.order.infrastructure.adapter;

import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import com.sparta.hotdeal.order.application.dtos.user.UserDto;
import com.sparta.hotdeal.order.application.port.UserClientPort;
import com.sparta.hotdeal.order.infrastructure.client.UserClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserClientPort {

    private final UserClient userClient;

    @Override
    public AddressDto getAddress(UUID userId, String email, String role, UUID addressId) {
        return userClient.getAddress(userId, email, role, addressId).getData().toAddressForOrderDto();
//        return AddressDto.createDummy();
    }

    @Override
    public UserDto getUserById(UUID userId, String email, String role) {
        return userClient.getUser(userId, email, role, userId).getData().toUserByIdForOrderDto();
//        return UserDto.createDummy();
    }
}
