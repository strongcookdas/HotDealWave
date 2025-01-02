package com.sparta.hotdeal.user.presentation.dtos.users.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResDeleteUsersByIdDto {
    private UUID userId;
}
