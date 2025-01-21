package com.sparta.hotdeal.product.infrastructure.dtos;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetUserByIdDto {
    private UUID userId;
    private String email;
    private String nickname;
    private UUID defaultAddressId;

    public static ResGetUserByIdDto createDummy() {
        return ResGetUserByIdDto.builder()
                .userId(UUID.randomUUID())
                .email("test@gtest.com")
                .nickname("테스트닉네임")
                .defaultAddressId(null)
                .build();
    }
}
