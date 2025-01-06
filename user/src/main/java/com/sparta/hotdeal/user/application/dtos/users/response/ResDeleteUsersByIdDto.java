package com.sparta.hotdeal.user.application.dtos.users.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResDeleteUsersByIdDto {
    private UUID userId;
}
