package com.sparta.hotdeal.company.application.dtos.company;

import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostCompanyDto {
    @NotNull
    private Long businessRegistrationNumber;
    @NotNull
    private Long companyPhoneNumber;
    @NotNull
    private UUID managerId;
    @NotNull
    private String brandName;
    @NotNull
    private String companyEmail;
    @NotNull
    private CompanyStatusEnum status;
}
