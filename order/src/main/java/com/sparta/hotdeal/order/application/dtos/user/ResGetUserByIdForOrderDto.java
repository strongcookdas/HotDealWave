package com.sparta.hotdeal.order.application.dtos.user;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetUserByIdForOrderDto {
    private UUID userId;
    private String email;
    private String nickname;

    public ResGetUserByIdForOrderDto create(
            UUID userId,
            String email,
            String nickname
    ) {
        return ResGetUserByIdForOrderDto.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .build();
    }

    public static ResGetUserByIdForOrderDto createDummy() {
        return ResGetUserByIdForOrderDto.builder()
                .userId(UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44"))
                .email("email@email.com")
                .nickname("nickname")
                .build();
    }
}
