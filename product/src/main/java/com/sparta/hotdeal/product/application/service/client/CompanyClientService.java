package com.sparta.hotdeal.product.application.service.client;

import com.sparta.hotdeal.product.infrastructure.dtos.ResGetCompanyByIdDto;
import java.util.UUID;

public interface CompanyClientService {

    ResGetCompanyByIdDto getCompany(UUID companyId);
}
