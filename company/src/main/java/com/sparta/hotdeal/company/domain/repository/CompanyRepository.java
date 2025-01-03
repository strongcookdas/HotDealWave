package com.sparta.hotdeal.company.domain.repository;

import com.sparta.hotdeal.company.domain.entity.Company;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface CompanyRepository {
    Company save(Company company);
}
