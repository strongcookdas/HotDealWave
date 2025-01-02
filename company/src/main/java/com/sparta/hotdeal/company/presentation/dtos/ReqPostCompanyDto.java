package com.sparta.hotdeal.company.presentation.dtos;

import com.sparta.hotdeal.company.domain.entity.CompanyStatusEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostCompanyDto {
    Long businessRegistrationNumber;
    Long companyPhoneNumber;
    UUID managerId;
    String brandName;
    String companyEmail;
    CompanyStatusEnum status;
}
