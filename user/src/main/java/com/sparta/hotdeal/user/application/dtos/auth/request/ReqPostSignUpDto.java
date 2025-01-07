package com.sparta.hotdeal.user.application.dtos.auth.request;

import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostSignUpDto {
    private String email;
    private String password;
    private String nickname;
    private UserRole role;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
