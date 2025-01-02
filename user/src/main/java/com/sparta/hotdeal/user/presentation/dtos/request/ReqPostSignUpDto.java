package com.sparta.hotdeal.user.presentation.dtos.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostSignUpDto {
    private String email;
    private String password;
    private String nickname;
}
