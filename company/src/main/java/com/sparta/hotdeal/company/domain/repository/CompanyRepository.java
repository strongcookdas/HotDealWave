package com.sparta.hotdeal.company.domain.repository;

import com.sparta.hotdeal.company.domain.entity.Company;
import com.sparta.hotdeal.company.infrastructure.repository.CompanyRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CompanyRepositoryImpl {
    Company save(Company company);
}
