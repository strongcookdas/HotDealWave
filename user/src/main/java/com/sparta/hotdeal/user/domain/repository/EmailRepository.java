package com.sparta.hotdeal.user.domain.repository;

import com.sparta.hotdeal.user.domain.entity.Email;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository {
    Email save(Email email);
    boolean existsById(String email);
}
