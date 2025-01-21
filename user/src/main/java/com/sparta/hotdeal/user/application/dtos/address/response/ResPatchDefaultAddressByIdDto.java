package com.sparta.hotdeal.user.application.dtos.address.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPatchDefaultAddressByIdDto {
    private UUID addressId;
}
