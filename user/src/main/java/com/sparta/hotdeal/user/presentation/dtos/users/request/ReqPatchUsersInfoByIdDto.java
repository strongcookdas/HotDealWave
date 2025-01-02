package com.sparta.hotdeal.user.presentation.dtos.users.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchUsersInfoByIdDto {
    private String nickname;
}
