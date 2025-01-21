package com.sparta.hotdeal.user.application.dtos.address.response;

import com.sparta.hotdeal.user.domain.entity.Address;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetAddressesDto {
    private UUID addressId;
    private String zipNum;
    private String city;
    private String district;
    private String streetName;
    private String streetNum;
    private String detailAddr;

    public static ResGetAddressesDto from(Address address) {
        return ResGetAddressesDto.builder()
                .addressId(address.getAddressId())
                .zipNum(address.getZipNum())
                .city(address.getCity())
                .district(address.getDistrict())
                .streetName(address.getStreetName())
                .streetNum(address.getStreetNum())
                .detailAddr(address.getDetailAddr())
                .build();
    }
}
