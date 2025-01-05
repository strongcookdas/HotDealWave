package com.sparta.hotdeal.company.application.dtos.company;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchCompanyByIdDto {
    private Long companyPhoneNumber;
    private UUID managerId;
    private String brandName;
    private String companyEmail;
}
