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
    public AddressDto getAddress(UUID addressId) {
        //return userClient.getAddressFromApi(addressId).getData().toAddressForOrderDto();
        return AddressDto.createDummy();
    }

    @Override
    public UserDto getUserById(UUID userId) {
        //return getUserFromApi(userId).getData().toUserByIdForOrderDto();
        return UserDto.createDummy();
    }
}
