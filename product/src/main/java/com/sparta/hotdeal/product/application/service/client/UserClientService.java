package com.sparta.hotdeal.product.application.service.client;

import com.sparta.hotdeal.product.infrastructure.dtos.ResGetUserByIdDto;
import java.util.UUID;

public interface UserClientService {
    ResGetUserByIdDto getUser(UUID userId);
}
