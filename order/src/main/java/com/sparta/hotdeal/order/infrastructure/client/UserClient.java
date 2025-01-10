package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.infrastructure.config.UserClientConfig;
import com.sparta.hotdeal.order.infrastructure.dtos.address.ResGetAddressByIdDto;
import com.sparta.hotdeal.order.infrastructure.dtos.user.ResGetUserByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", configuration = UserClientConfig.class)
public interface UserClient {
    @GetMapping(value = "/api/v1/address/{addressId}")
    ResponseDto<ResGetAddressByIdDto> getAddress(@RequestHeader("X-User-UserId") UUID userId,
                                                 @RequestHeader("X-User-Email") String email,
                                                 @RequestHeader("X-User-Role") String role,
                                                 @PathVariable UUID addressId);

    @GetMapping(value = "/api/v1/users/{userId}")
    ResponseDto<ResGetUserByIdDto> getUser(@RequestHeader("X-User-UserId") UUID xUserId,
                                           @RequestHeader("X-User-Email") String email,
                                           @RequestHeader("X-User-Role") String role,
                                           @PathVariable UUID userId);
}
