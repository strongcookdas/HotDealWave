package com.sparta.hotdeal.user.application.service;

import com.sparta.hotdeal.user.application.dtos.address.request.ReqPostAddressDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResGetAddressByIdDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResGetDefaultAddressDto;
import com.sparta.hotdeal.user.application.dtos.address.response.ResPostAddressDto;
import com.sparta.hotdeal.user.application.exception.ErrorMessage;
import com.sparta.hotdeal.user.domain.entity.Address;
import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.repository.AddressRepository;
import com.sparta.hotdeal.user.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public ResPostAddressDto createAddress(ReqPostAddressDto requestDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElse(null);
        Address address = addressRepository.save(requestDto.toEntity(user));

        return ResPostAddressDto.builder()
                .addressId(address.getAddressId())
                .build();
    }

    public ResGetAddressByIdDto getAddress(UUID addressId, UUID userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ALLOWED_CONTENT.getMessage());
        }

        return ResGetAddressByIdDto.from(address);
    }

    public ResGetDefaultAddressDto getDefaultAddress(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        Address address = user.getDefaultAddress();

        return ResGetDefaultAddressDto.from(address);
    }
}
