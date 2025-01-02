package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.presentation.dtos.ResponseDto;
import com.sparta.hotdeal.user.presentation.dtos.users.request.ReqPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.request.ReqPatchUsersPasswordByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResGetUserDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResPatchUsersInfoByIdDto;
import com.sparta.hotdeal.user.presentation.dtos.users.response.ResPatchUsersPasswordByIdDto;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto<ResGetUserDto>> getUser(@PathVariable("userId") UUID userId) {

        ResGetUserDto resGetUserDto = ResGetUserDto.builder()
                .userId(userId)
                .email("example@email.com")
                .nickname("exampleNickname")
                .defaultAddressId(UUID.randomUUID())
                .build();

        ResponseDto<ResGetUserDto> responseDto = ResponseDto.of(200, "회원정보조회 성공", resGetUserDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseDto<ResPatchUsersInfoByIdDto>> changeUserInfos(
            @PathVariable("userId") UUID userId,
            @RequestBody ReqPatchUsersInfoByIdDto reqPatchUsersInfoByIdDto
    ) {

        ResPatchUsersInfoByIdDto resPatchUsersInfoByIdDto = ResPatchUsersInfoByIdDto.builder()
                .userId(userId)
                .build();

        ResponseDto<ResPatchUsersInfoByIdDto> responseDto = ResponseDto.of(200, "회원정보수정 성공", resPatchUsersInfoByIdDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseDto<ResPatchUsersPasswordByIdDto>> changeUserInfos(
            @PathVariable("userId") UUID userId,
            @RequestBody ReqPatchUsersPasswordByIdDto reqPatchUsersPasswordByIdDto
    ) {

        ResPatchUsersPasswordByIdDto resPatchUsersPasswordByIdDto = ResPatchUsersPasswordByIdDto.builder()
                .userId(userId)
                .build();

        ResponseDto<ResPatchUsersPasswordByIdDto> responseDto = ResponseDto.of(200, "비밀번호수정 성공",
                resPatchUsersPasswordByIdDto);

        return ResponseEntity.ok(responseDto);
    }
}
