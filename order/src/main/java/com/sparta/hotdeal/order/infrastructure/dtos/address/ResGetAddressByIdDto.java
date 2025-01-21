package com.sparta.hotdeal.order.infrastructure.dtos.address;

import com.sparta.hotdeal.order.application.dtos.address.AddressDto;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetAddressByIdDto {
    private UUID addressId;
    private String zipNum;
    private String city;
    private String district;
    private String streetName;
    private String streetNum;
    private String detailAddr;

    public AddressDto toAddressForOrderDto() {
        return AddressDto.create(
                addressId,
                zipNum,
                city,
                district,
                streetName,
                streetNum,
                detailAddr
        );
    }
}
