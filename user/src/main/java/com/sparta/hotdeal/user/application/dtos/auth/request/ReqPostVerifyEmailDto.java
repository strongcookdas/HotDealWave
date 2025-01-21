package com.sparta.hotdeal.user.application.dtos.auth.request;

import com.sparta.hotdeal.user.domain.entity.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostVerifyEmailDto {
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank
    private String email;

    public Email toEntity() {
        return Email.builder()
                .email(email)
                .verified(false)
                .build();
    }
}
