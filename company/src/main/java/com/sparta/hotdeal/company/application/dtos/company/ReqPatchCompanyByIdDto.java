package com.sparta.hotdeal.company.application.dtos.company;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchCompanyByIdDto {
    @NotNull
    private Long companyPhoneNumber;
    @NotNull
    private UUID managerId;
    @NotNull
    private String brandName;
    @NotNull
    private String companyEmail;
}
