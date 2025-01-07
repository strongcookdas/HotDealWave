package com.sparta.hotdeal.order.infrastructure.dtos.user;

import com.sparta.hotdeal.order.application.dtos.user.UserDto;
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

    public UserDto toUserByIdForOrderDto() {
        return UserDto.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
