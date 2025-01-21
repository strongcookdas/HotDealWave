package com.sparta.hotdeal.user.infrastructure.repository;

import com.sparta.hotdeal.user.domain.entity.Email;
import com.sparta.hotdeal.user.domain.repository.EmailRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepositoryImpl extends JpaRepository<Email, String>, EmailRepository {
}
