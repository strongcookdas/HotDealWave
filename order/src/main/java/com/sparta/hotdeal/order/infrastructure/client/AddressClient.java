package com.sparta.hotdeal.order.infrastructure.client;

import com.sparta.hotdeal.order.application.dtos.ResponseDto;
import com.sparta.hotdeal.order.application.dtos.address.res.ResGetAddressByIdForOrderDto;
import com.sparta.hotdeal.order.application.service.client.AddressClientService;
import com.sparta.hotdeal.order.infrastructure.dtos.address.ResGetAddressByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface AddressClient extends AddressClientService {
    @GetMapping(value = "/api/v1/address/{addressId}")
    ResponseDto<ResGetAddressByIdDto> getAddressFromApi(@PathVariable UUID addressId);
    @Override
    default ResGetAddressByIdForOrderDto getAddress(UUID addressId){
        return getAddressFromApi(addressId).getData().toAddressForOrderDto();
    }
}
