package com.sparta.hotdeal.company.presentation.dtos;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchCompanyByIdDto {
    Long companyPhoneNumber;
    UUID managerId;
    String brandName;
    String companyEmail;
}
