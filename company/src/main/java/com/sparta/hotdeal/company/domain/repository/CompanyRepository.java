package com.sparta.hotdeal.company.domain.repository;

import com.sparta.hotdeal.company.domain.entity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository {
    Company save(Company company);
}
