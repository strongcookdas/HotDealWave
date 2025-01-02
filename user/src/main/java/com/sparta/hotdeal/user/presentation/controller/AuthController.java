package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.presentation.dtos.request.ReqPostCheckEmailDto;
import com.sparta.hotdeal.user.presentation.dtos.request.ReqPostSignUpDto;
import com.sparta.hotdeal.user.presentation.dtos.response.ResPostSignUpDto;
import com.sparta.hotdeal.user.presentation.dtos.ResponseDto;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("signup")
    public ResponseEntity<ResponseDto<ResPostSignUpDto>> signUp(@RequestBody ReqPostSignUpDto requestDto) {

        ResPostSignUpDto resPostSignUpDto = ResPostSignUpDto.builder()
                .userId(UUID.randomUUID())
                .build();

        ResponseDto<ResPostSignUpDto> responseDto = ResponseDto.of(201, "회원가입 성공", resPostSignUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("check-email")
    public ResponseEntity<ResponseDto<Void>> checkEmail(@RequestBody ReqPostCheckEmailDto requestDto) {

        ResponseDto<Void> responseDto = ResponseDto.of(200, "사용할 수 있는 이메일입니다.", null);

        return ResponseEntity.ok(responseDto);
    }
}
