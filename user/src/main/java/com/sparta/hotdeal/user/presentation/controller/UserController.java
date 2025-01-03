package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.presentation.dtos.ResponseDto;
import com.sparta.hotdeal.user.presentation.dtos.users.request.ReqPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.request.ReqPatchUsersPasswordByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResDeleteUsersByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResGetUsersByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResPatchUsersPasswordByIdDto;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @GetMapping("/{userId}")
    public ResponseDto<ResGetUsersByIdDto> getUser(@PathVariable("userId") UUID userId) {

        ResGetUsersByIdDto resGetUsersByIdDto = ResGetUsersByIdDto.builder()
                .userId(userId)
                .email("example@email.com")
                .nickname("exampleNickname")
                .defaultAddressId(UUID.randomUUID())
                .build();

        return ResponseDto.of("회원정보조회 성공", resGetUsersByIdDto);
    }

    @PatchMapping("/{userId}")
    public ResponseDto<ResPatchUsersInfoByIdDto> changeUserInfos(
            @PathVariable("userId") UUID userId,
            @RequestBody ReqPatchUsersInfoByIdDto reqPatchUsersInfoByIdDto
    ) {

        ResPatchUsersInfoByIdDto resPatchUsersInfoByIdDto = ResPatchUsersInfoByIdDto.builder()
                .userId(userId)
                .build();

        return ResponseDto.of("회원정보수정 성공", resPatchUsersInfoByIdDto);
    }

    @PatchMapping("/{userId}/password")
    public ResponseDto<ResPatchUsersPasswordByIdDto> changeUserPassword(
            @PathVariable("userId") UUID userId,
            @RequestBody ReqPatchUsersPasswordByIdDto reqPatchUsersPasswordByIdDto
    ) {

        ResPatchUsersPasswordByIdDto resPatchUsersPasswordByIdDto = ResPatchUsersPasswordByIdDto.builder()
                .userId(userId)
                .build();

        return ResponseDto.of("비밀번호수정 성공", resPatchUsersPasswordByIdDto);

    }

    @DeleteMapping("/{userId}")
    public ResponseDto<ResDeleteUsersByIdDto> deleteUser(@PathVariable("userId") UUID userId) {

        ResDeleteUsersByIdDto resDeleteUsersByIdDto = ResDeleteUsersByIdDto.builder()
                .userId(userId)
                .build();

        return ResponseDto.of("회원탈퇴 성공", resDeleteUsersByIdDto);
    }
}
