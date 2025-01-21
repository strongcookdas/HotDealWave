package com.sparta.hotdeal.company.infrastructure.repository;

import com.sparta.hotdeal.company.domain.entity.company.Company;
import com.sparta.hotdeal.company.domain.repository.CompanyRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepositoryImpl extends JpaRepository<Company, UUID>, CompanyRepository {
}
