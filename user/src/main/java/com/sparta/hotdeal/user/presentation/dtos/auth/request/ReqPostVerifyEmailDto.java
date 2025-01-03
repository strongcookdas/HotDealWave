package com.sparta.hotdeal.user.presentation.dtos.auth.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostVerifyEmailDto {
    private String email;
}
