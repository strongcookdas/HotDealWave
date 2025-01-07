package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.dtos.address.ResGetAddressByIdDto;
import com.sparta.hotdeal.order.infrastructure.dtos.user.ResGetUserByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient{
    @GetMapping(value = "/api/v1/address/{addressId}")
    ResponseDto<ResGetAddressByIdDto> getAddressFromApi(@PathVariable UUID addressId);

    @GetMapping(value = "/api/v1/users/{userId}")
    ResponseDto<ResGetUserByIdDto> getUserFromApi(@PathVariable UUID userId);
}
