package com.sparta.hotdeal.user.application.service;

import com.sparta.hotdeal.user.application.dtos.users.request.ReqPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.request.ReqPatchUsersPasswordByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResGetUsersByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResPatchUsersPasswordByIdDto;
import com.sparta.hotdeal.user.application.exception.ErrorMessage;
import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResGetUsersByIdDto getUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        return ResGetUsersByIdDto.from(user);
    }

    @Transactional
    public ResPatchUsersInfoByIdDto updateUser(UUID userId, ReqPatchUsersInfoByIdDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        user.updateUser(requestDto.getNickname());

        return ResPatchUsersInfoByIdDto.builder()
                .userId(user.getUserId())
                .build();
    }

    @Transactional
    public ResPatchUsersPasswordByIdDto updateUserPassword(UUID userId, ReqPatchUsersPasswordByIdDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        user.updatePassword(encodedPassword);

        return ResPatchUsersPasswordByIdDto.builder()
                .userId(user.getUserId())
                .build();
    }
}
