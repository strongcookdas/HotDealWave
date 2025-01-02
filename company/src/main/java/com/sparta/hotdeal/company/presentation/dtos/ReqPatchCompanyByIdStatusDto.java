package com.sparta.hotdeal.company.presentation.dtos;

import com.sparta.hotdeal.company.domain.entity.CompanyStatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPatchCompanyByIdStatusDto {
    CompanyStatusEnum status;
}
