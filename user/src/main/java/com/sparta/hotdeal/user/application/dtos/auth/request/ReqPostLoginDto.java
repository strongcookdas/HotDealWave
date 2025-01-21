package com.sparta.hotdeal.user.application.dtos.auth.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostLoginDto {
    private String email;
    private String password;
}
