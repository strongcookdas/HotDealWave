package com.sparta.hotdeal.company.application.dtos.company;

import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostCompanyDto {
    private Long businessRegistrationNumber;
    private Long companyPhoneNumber;
    private UUID managerId;
    private String brandName;
    private String companyEmail;
    private CompanyStatusEnum status;
}
