package com.sparta.hotdeal.company.application.dtos.company;

import com.sparta.hotdeal.company.domain.entity.company.Company;
import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResGetCompanyByIdDto {
    private Long businessRegistrationNumber;
    private Long companyPhoneNumber;
    private UUID managerId;
    private String brandName;
    private String companyEmail;
    private CompanyStatusEnum status;

    public static ResGetCompanyByIdDto create(Company company) {
        return ResGetCompanyByIdDto.builder()
                .businessRegistrationNumber(company.getBusinessRegistrationNumber())
                .companyPhoneNumber(company.getCompanyPhoneNumber())
                .managerId(company.getManagerId())
                .brandName(company.getBrandName())
                .companyEmail(company.getCompanyEmail())
                .status(company.getStatus())
                .build();
    }
}
