package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.presentation.dtos.ResponseDto;
import com.sparta.hotdeal.user.presentation.dtos.address.request.ReqPatchAddressByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.address.request.ReqPostAddressDto;
import com.sparta.hotdeal.user.presentation.dtos.address.response.ResGetAddressByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.address.response.ResGetAddressesDto;
import com.sparta.hotdeal.user.presentation.dtos.address.response.ResGetDefaultAddressDto;
import com.sparta.hotdeal.user.presentation.dtos.address.response.ResPatchAddressByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.address.response.ResPostAddressDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AddressController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostAddressDto> createAddress(@RequestBody ReqPostAddressDto requestDto) {

        ResPostAddressDto resPostAddressDto = ResPostAddressDto.builder()
                .addressId(UUID.randomUUID())
                .build();

        return ResponseDto.of("배송지 등록 성공", resPostAddressDto);
    }

    @GetMapping("/address/{addressId}")
    public ResponseDto<ResGetAddressByIdDto> getAddress(@PathVariable UUID addressId) {

        ResGetAddressByIdDto resGetAddressByIdDto = ResGetAddressByIdDto.builder()
                .addressId(addressId)
                .zipNum("zipNum")
                .city("city")
                .district("district")
                .streetName("streetName")
                .streetNum("streetNum")
                .detailAddr("detailAddr")
                .build();

        return ResponseDto.of("배송지 조회 성공", resGetAddressByIdDto);
    }

    @GetMapping("/address-default")
    public ResponseDto<ResGetDefaultAddressDto> getDefaultAddress() {

        ResGetDefaultAddressDto resGetDefaultAddressDto = ResGetDefaultAddressDto.builder()
                .addressId(UUID.randomUUID())
                .zipNum("zipNum")
                .city("city")
                .district("district")
                .streetName("streetName")
                .streetNum("streetNum")
                .detailAddr("detailAddr")
                .build();

        return ResponseDto.of("기본 배송지 조회 성공", resGetDefaultAddressDto);
    }

    // 구현 시 List가 아닌 페이징을 적용하도록 수정
    @GetMapping("/address")
    public ResponseDto<List<ResGetAddressesDto>> getAddresses() {

        ResGetAddressesDto resGetAddressesDto = ResGetAddressesDto.builder()
                .addressId(UUID.randomUUID())
                .zipNum("zipNum")
                .city("city")
                .district("district")
                .streetName("streetName")
                .streetNum("streetNum")
                .detailAddr("detailAddr")
                .build();

        List<ResGetAddressesDto> responseDtos = new ArrayList<>();
        responseDtos.add(resGetAddressesDto);

        return ResponseDto.of("배송지 목록 조회 성공", responseDtos);
    }

    @PatchMapping("/address/{addressId}")
    public ResponseDto<ResPatchAddressByIdDto> updateAddress(@PathVariable UUID addressId, @RequestBody ReqPatchAddressByIdDto requestDto) {

        ResPatchAddressByIdDto resPatchAddressByIdDto = ResPatchAddressByIdDto.builder()
                .addressId(addressId)
                .build();

        return ResponseDto.of("배송지 수정 성공", resPatchAddressByIdDto);
    }
}
