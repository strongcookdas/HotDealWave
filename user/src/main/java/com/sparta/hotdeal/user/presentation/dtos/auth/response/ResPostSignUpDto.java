package com.sparta.hotdeal.user.presentation.dtos.auth.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostSignUpDto {
    private UUID userId;
}
