package com.sparta.hotdeal.order.application.dtos.address.res;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ResGetAddressByIdForOrderDto {
    private UUID addressId;
    private String zipNum;
    private String city;
    private String district;
    private String streetName;
    private String streetNum;
    private String detailAddr;

    public static ResGetAddressByIdForOrderDto create(
            UUID addressId,
            String zipNum,
            String city,
            String district,
            String streetName,
            String streetNum,
            String detailAddr
    ) {
        return ResGetAddressByIdForOrderDto.builder()
                .addressId(addressId)
                .zipNum(zipNum)
                .city(city)
                .district(district)
                .streetName(streetName)
                .streetNum(streetNum)
                .detailAddr(detailAddr)
                .build();
    }
}
