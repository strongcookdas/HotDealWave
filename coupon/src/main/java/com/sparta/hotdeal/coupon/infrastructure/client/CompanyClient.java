package com.sparta.hotdeal.coupon.infrastructure.client;

import com.sparta.hotdeal.coupon.application.dto.ResponseDto;
import com.sparta.hotdeal.coupon.application.service.client.CompanyClientService;
import com.sparta.hotdeal.coupon.infrastructure.dto.ResGetCompanyByIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service")
public interface CompanyClient extends CompanyClientService {

    @GetMapping("/api/v1/companies/{companyId}")
    ResponseDto<ResGetCompanyByIdDto> getCompanyById(@PathVariable("companyId") UUID companyId);

    @Override
    default ResGetCompanyByIdDto getCompanyDataById(UUID companyId) {
        return this.getCompanyById(companyId).getData();
    }
}
