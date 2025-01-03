package com.sparta.hotdeal.company.application.service;

import com.sparta.hotdeal.company.application.dtos.ReqPostCompanyDto;
import com.sparta.hotdeal.company.domain.entity.Company;
import com.sparta.hotdeal.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
}
