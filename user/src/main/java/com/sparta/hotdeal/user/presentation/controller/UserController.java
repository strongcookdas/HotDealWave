package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.application.dtos.ResponseDto;
import com.sparta.hotdeal.user.application.dtos.ResponseMessage;
import com.sparta.hotdeal.user.application.dtos.users.request.ReqPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.request.ReqPatchUsersPasswordByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResDeleteUsersByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResGetUsersByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.application.dtos.users.response.ResPatchUsersPasswordByIdDto;
import com.sparta.hotdeal.user.application.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseDto<ResGetUsersByIdDto> getUser(@PathVariable("userId") UUID userId) {

        ResGetUsersByIdDto resGetUsersByIdDto = userService.getUser(userId);

        return ResponseDto.of(ResponseMessage.GET_USER_SUCCESS.getMessage(), resGetUsersByIdDto);
    }

    @PatchMapping("/{userId}")
    public ResponseDto<ResPatchUsersInfoByIdDto> updateUserInfos(
            @PathVariable("userId") UUID userId,
            @RequestBody ReqPatchUsersInfoByIdDto reqPatchUsersInfoByIdDto
    ) {

        ResPatchUsersInfoByIdDto resPatchUsersInfoByIdDto = userService.updateUser(userId, reqPatchUsersInfoByIdDto);

        return ResponseDto.of(ResponseMessage.UPDATE_USER_SUCCESS.getMessage(), resPatchUsersInfoByIdDto);
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
