package com.sparta.hotdeal.company.application.service;

import com.sparta.hotdeal.company.application.dtos.company.ReqPostCompanyDto;
import com.sparta.hotdeal.company.domain.entity.company.Company;
import com.sparta.hotdeal.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public void createCompany(ReqPostCompanyDto reqPostCompanyDto) {

        Company company = Company.create(
                reqPostCompanyDto.getBusinessRegistrationNumber(),
                reqPostCompanyDto.getCompanyPhoneNumber(),
                reqPostCompanyDto.getManagerId(),
                reqPostCompanyDto.getCompanyEmail(),
                reqPostCompanyDto.getBrandName(),
                reqPostCompanyDto.getStatus()
        );
        companyRepository.save(company);
    }
}
