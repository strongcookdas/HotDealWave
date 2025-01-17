package com.sparta.hotdeal.company.domain.repository;

import com.sparta.hotdeal.company.domain.entity.company.Company;
import com.sparta.hotdeal.company.domain.entity.company.CompanyStatusEnum;
import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository {
    Company save(Company company);

    Optional<Company> findByBusinessRegistrationNumber(Long businessRegistrationNumber);

    Optional<Company> findById(UUID id);

    Page<Company> findAll(Pageable pageable);

    Page<Company> findByStatus(CompanyStatusEnum status, Pageable pageable);
}
