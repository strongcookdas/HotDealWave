package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.application.dtos.ResponseDto;
import com.sparta.hotdeal.user.application.dtos.ResponseMessage;
import com.sparta.hotdeal.user.application.dtos.address.request.ReqPatchAddressByIdDto;
import com.sparta.hotdeal.user.application.dtos.address.request.ReqPostAddressDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResDeleteAddressByIdDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResGetAddressByIdDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResGetAddressesDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResGetDefaultAddressDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResPatchAddressByIdDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResPatchDefaultAddressByIdDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResPostAddressDto;
import com.sparta.hotdeal.user.application.service.AddressService;
import com.sparta.hotdeal.user.infrastructure.security.RequestUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostAddressDto> createAddress(
            @RequestBody ReqPostAddressDto requestDto,
            @AuthenticationPrincipal RequestUserDetails userDetails
    ) {

        ResPostAddressDto resPostAddressDto = addressService.createAddress(requestDto, userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.CREATE_ADDRESS_SUCCESS.getMessage(), resPostAddressDto);
    }

    @GetMapping("/address/{addressId}")
    public ResponseDto<ResGetAddressByIdDto> getAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal RequestUserDetails userDetails
    ) {

        ResGetAddressByIdDto resGetAddressByIdDto = addressService.getAddress(addressId, userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.GET_ADDRESS_SUCCESS.getMessage(), resGetAddressByIdDto);
    }

    @GetMapping("/address-default")
    public ResponseDto<ResGetDefaultAddressDto> getDefaultAddress(
            @AuthenticationPrincipal RequestUserDetails userDetails
    ) {

        ResGetDefaultAddressDto resGetDefaultAddressDto = addressService.getDefaultAddress(userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.GET_DEFAULT_ADDRESS_SUCCESS.getMessage(), resGetDefaultAddressDto);
    }

    @GetMapping("/address")
    public ResponseDto<PagedModel<ResGetAddressesDto>> getAddresses(
            Pageable pageable,
            @AuthenticationPrincipal RequestUserDetails userDetails
    ) {

        PagedModel<ResGetAddressesDto> resGetAddressesDtos =
                addressService.getAddresses(pageable, userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.GET_ADDRESS_LIST_SUCCESS.getMessage(), resGetAddressesDtos);
    }

    @PatchMapping("/address/{addressId}")
    public ResponseDto<ResPatchAddressByIdDto> updateAddress(
            @PathVariable UUID addressId,
            @RequestBody ReqPatchAddressByIdDto requestDto,
            @AuthenticationPrincipal RequestUserDetails userDetails) {

        ResPatchAddressByIdDto resPatchAddressByIdDto =
                addressService.updateAddress(addressId, requestDto, userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.UPDATE_ADDRESS_SUCCESS.getMessage(), resPatchAddressByIdDto);
    }

    @PatchMapping("/address-default/{addressId}")
    public ResponseDto<ResPatchDefaultAddressByIdDto> updateDefaultAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal RequestUserDetails userDetails
    ) {

        ResPatchDefaultAddressByIdDto resPatchDefaultAddressByIdDto =
                addressService.updateDefaultAddress(addressId, userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.UPDATE_DEFAULT_ADDRESS_SUCCESS.getMessage(),
                resPatchDefaultAddressByIdDto);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseDto<ResDeleteAddressByIdDto> deleteAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal RequestUserDetails userDetails
    ) {

        ResDeleteAddressByIdDto resDeleteAddressByIdDto =
                addressService.deleteAddress(addressId, userDetails.getUserId());

        return ResponseDto.of(ResponseMessage.DELETE_ADDRESS_SUCCESS.getMessage(), resDeleteAddressByIdDto);
    }
}
