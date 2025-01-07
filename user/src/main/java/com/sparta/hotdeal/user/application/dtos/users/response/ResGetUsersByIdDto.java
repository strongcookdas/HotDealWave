package com.sparta.hotdeal.user.application.dtos.users.response;

import com.sparta.hotdeal.user.domain.entity.User;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetUsersByIdDto {
    private UUID userId;
    private String email;
    private String nickname;
    private UUID defaultAddressId;

    public static ResGetUsersByIdDto from(User user) {
        return ResGetUsersByIdDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .defaultAddressId(null)
                .build();
    }
}
