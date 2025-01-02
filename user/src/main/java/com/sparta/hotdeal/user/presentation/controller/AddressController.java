package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.presentation.dtos.ResponseDto;
import com.sparta.hotdeal.user.presentation.dtos.address.request.ReqPostAddressDto;
import com.sparta.hotdeal.user.presentation.dtos.address.response.ResPostAddressDto;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    @PostMapping
    public ResponseDto<ResPostAddressDto> createAddress(@RequestBody ReqPostAddressDto requestDto) {

        ResPostAddressDto resPostAddressDto = ResPostAddressDto.builder()
                .addressId(UUID.randomUUID())
                .build();

        return ResponseDto.of("배송지 등록 성공", resPostAddressDto);
    }
}
