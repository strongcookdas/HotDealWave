package com.sparta.hotdeal.user.infrastructure.repository;

import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.repository.UserRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryImpl extends JpaRepository<User, UUID>, UserRepository {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
