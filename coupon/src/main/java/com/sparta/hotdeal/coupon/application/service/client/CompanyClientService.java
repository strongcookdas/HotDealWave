package com.sparta.hotdeal.coupon.application.service.client;

import com.sparta.hotdeal.coupon.infrastructure.dto.ResGetCompanyByIdDto;

import java.util.UUID;

public interface CompanyClientService {

    ResGetCompanyByIdDto getCompanyDataById(UUID companyId);
}
