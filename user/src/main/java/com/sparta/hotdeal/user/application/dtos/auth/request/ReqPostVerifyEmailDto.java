package com.sparta.hotdeal.user.application.dtos.auth.request;

import com.sparta.hotdeal.user.domain.entity.Email;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostVerifyEmailDto {
    private String email;

    public Email toEntity() {
        return Email.builder()
                .email(email)
                .verified(false)
                .build();
    }
}
