package com.sparta.hotdeal.user.presentation.dtos.address.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostAddressDto {
    private String zipNum;
    private String city;
    private String district;
    private String streetName;
    private String streetNum;
    private String detailAddr;
}
