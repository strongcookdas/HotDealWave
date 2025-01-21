package com.sparta.hotdeal.company.application.dtos.company;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchCompanyByIdDto {
    @NotNull(message = "업체 번호를 입력해주세요.")
    private Long companyPhoneNumber;
    @NotNull(message = "관리자 ID를 입력해주세요.")
    private UUID managerId;
    @NotNull(message = "브랜드이름을 입력해주세요.")
    private String brandName;
    @NotNull(message = "업체 이메일을 입력해주세요.")
    private String companyEmail;
}
