package com.sparta.hotdeal.company.domain.repository;

import com.sparta.hotdeal.company.domain.entity.company.Company;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository {
    Company save(Company company);

    Optional<Company> findByBusinessRegistrationNumber(Long businessRegistrationNumber);

    Optional<Company> findById(UUID id);

}
