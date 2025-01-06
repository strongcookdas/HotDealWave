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

    public static ResGetAddressByIdForOrderDto createDummy() {
        return ResGetAddressByIdForOrderDto.builder()
                .addressId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")) // 예제 UUID
                .zipNum("12345") // 예제 우편번호
                .city("Seoul") // 예제 도시
                .district("Gangnam-gu") // 예제 구
                .streetName("Teheran-ro") // 예제 도로명
                .streetNum("123") // 예제 번지수
                .detailAddr("Apt 101") // 예제 상세 주소
                .build();
    }
}
