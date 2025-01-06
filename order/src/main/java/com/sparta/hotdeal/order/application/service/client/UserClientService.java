package com.sparta.hotdeal.order.application.service.client;

import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
import com.sparta.hotdeal.order.application.dtos.user.ResGetUserByIdForOrderDto;
import java.util.UUID;

public interface UserClientService {
    ResGetAddressByIdForOrderDto getAddress(UUID addressId);
    ResGetUserByIdForOrderDto getUserById(UUID userId);
}
