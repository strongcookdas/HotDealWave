package com.sparta.hotdeal.user.application.service;

import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostSignUpDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostSignUpDto;
import com.sparta.hotdeal.user.application.exception.ErrorMessage;
import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.entity.UserRole;
import com.sparta.hotdeal.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResPostSignUpDto signup(ReqPostSignUpDto requestDto) {
        checkNickname(requestDto.getNickname());
        checkRole(requestDto.getRole());

        User user = requestDto.toEntity(passwordEncoder);
        userRepository.save(user);

        return ResPostSignUpDto.builder()
                .userId(user.getUserId())
                .build();
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_EMAIL.getMessage());
        }
    }

    private void checkNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_NICKNAME.getMessage());
        }
    }

    private void checkRole(UserRole role) {
        if (role.toValue().equals("MANAGER") || role.toValue().equals("MASTER")) {
            throw new IllegalArgumentException(ErrorMessage.NOT_ALLOWED_ROLE.getMessage());
        }
    }
}
