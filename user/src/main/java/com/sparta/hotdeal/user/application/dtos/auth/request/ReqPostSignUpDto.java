package com.sparta.hotdeal.user.application.dtos.auth.request;

import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
public class ReqPostSignUpDto {
    private String email;
    private String password;
    private String nickname;
    private UserRole role;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .role(role)
                .build();
    }
}
