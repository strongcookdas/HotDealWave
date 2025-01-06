package com.sparta.hotdeal.product.infrastructure.client;

import com.sparta.hotdeal.product.application.dtos.res.ResponseDto;
import com.sparta.hotdeal.product.application.service.client.CompanyClientService;
import com.sparta.hotdeal.product.infrastructure.dtos.ResGetCompanyByIdDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface CompanyClient extends CompanyClientService {

    @GetMapping("/api/v1/companies/{companyId}")
    ResponseDto<ResGetCompanyByIdDto> getCompanyById(@PathVariable("companyId") UUID companyId);

    @Override
    default ResGetCompanyByIdDto getCompany(UUID companyId) {
//        return this.getCompanyById(companyId).getData();
        return ResGetCompanyByIdDto.createDummy();
    }
}
