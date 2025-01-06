package com.sparta.hotdeal.order.infrastructure.dtos.address;

import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
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

    public ResGetAddressByIdForOrderDto toAddressForOrderDto() {
        return ResGetAddressByIdForOrderDto.create(
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
