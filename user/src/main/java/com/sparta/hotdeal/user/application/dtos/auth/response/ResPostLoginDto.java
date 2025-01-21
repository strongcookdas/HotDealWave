package com.sparta.hotdeal.user.application.dtos.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostLoginDto {
    private String accessToken;
    private String refreshToken;
}
