package com.sparta.hotdeal.company.application.dtos.company;

import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchCompanyByIdStatusDto {
    @NotNull(message = "상태를 입력해주세요.")
    private CompanyStatusEnum status;
}
