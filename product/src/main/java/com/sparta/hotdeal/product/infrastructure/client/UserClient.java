package com.sparta.hotdeal.product.infrastructure.client;

import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.service.client.UserClientService;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetUserByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient extends UserClientService {

    @GetMapping("/api/v1/users/{userId}")
    ResponseDto<ResGetUserByIdDto> getUserById(@PathVariable("userId") UUID userId);

    @Override
    default ResGetUserByIdDto getUser(UUID userId) {
        return ResGetUserByIdDto.createDummy();
    }
}
