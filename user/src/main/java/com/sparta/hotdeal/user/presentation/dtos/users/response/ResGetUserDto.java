package com.sparta.hotdeal.user.presentation.dtos.users.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetUserDto {
    private UUID userId;
    private String email;
    private String nickname;
    private UUID defaultAddressId;
}
