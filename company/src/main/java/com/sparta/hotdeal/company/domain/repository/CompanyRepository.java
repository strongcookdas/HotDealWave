package com.sparta.hotdeal.company.domain.repository;

import com.sparta.hotdeal.company.domain.entity.company.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository {
    Company save(Company company);
}
