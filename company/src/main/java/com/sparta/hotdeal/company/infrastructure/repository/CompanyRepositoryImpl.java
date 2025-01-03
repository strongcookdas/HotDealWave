package com.sparta.hotdeal.company.infrastructure.repository;

import com.sparta.hotdeal.company.domain.entity.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepositoryImpl extends JpaRepository<Company, UUID> {
}
