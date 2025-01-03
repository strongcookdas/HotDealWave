package com.sparta.hotdeal.user.presentation.dtos.address.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostAddressDto {
    private UUID addressId;
}
