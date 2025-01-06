package com.sparta.hotdeal.order.application.service.client;

import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
import java.util.UUID;

public interface AddressClientService {
    ResGetAddressByIdForOrderDto getAddress(UUID addressId);
}
