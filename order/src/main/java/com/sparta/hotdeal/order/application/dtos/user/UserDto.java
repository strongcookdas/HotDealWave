package com.sparta.hotdeal.order.application.dtos.user;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private UUID userId;
    private String email;
    private String nickname;

    public UserDto create(
            UUID userId,
            String email,
            String nickname
    ) {
        return UserDto.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .build();
    }

    public static UserDto createDummy() {
        return UserDto.builder()
                .userId(UUID.fromString("8fbd655f-dc52-4bf9-ab23-ef89e923db44"))
                .email("email@email.com")
                .nickname("nickname")
                .build();
    }
}
