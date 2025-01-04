package com.sparta.hotdeal.user.domain.repository;

import com.sparta.hotdeal.user.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
