package com.sparta.hotdeal.user.application.dtos.address.request;

import com.sparta.hotdeal.user.domain.entity.Address;
import com.sparta.hotdeal.user.domain.entity.User;
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

    public Address toEntity(User user) {
        return Address.builder()
                .user(user)
                .zipNum(zipNum)
                .city(city)
                .district(district)
                .streetName(streetName)
                .streetNum(streetNum)
                .detailAddr(detailAddr)
                .build();
    }
}
