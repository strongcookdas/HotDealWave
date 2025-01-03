package com.sparta.hotdeal.user.application.dtos.auth.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostSignUpDto {
    private String email;
    private String password;
    private String nickname;
}
