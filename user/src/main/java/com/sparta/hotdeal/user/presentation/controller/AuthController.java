package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.presentation.dtos.ResponseDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.request.ReqPostCheckEmailDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.request.ReqPostConfirmEmailDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.request.ReqPostLoginDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.request.ReqPostSignUpDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.request.ReqPostVerifyEmailDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.response.ResPostLoginDto;
import com.sparta.hotdeal.user.presentation.dtos.auth.response.ResPostSignUpDto;
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

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<ResPostSignUpDto>> signUp(@RequestBody ReqPostSignUpDto requestDto) {

        ResPostSignUpDto resPostSignUpDto = ResPostSignUpDto.builder()
                .userId(UUID.randomUUID())
                .build();

        ResponseDto<ResPostSignUpDto> responseDto = ResponseDto.of(201, "회원가입 성공", resPostSignUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/check-email")
    public ResponseEntity<ResponseDto<Void>> checkEmail(@RequestBody ReqPostCheckEmailDto requestDto) {

        ResponseDto<Void> responseDto = ResponseDto.of(200, "사용할 수 있는 이메일입니다.", null);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ResponseDto<Void>> verifyEmail(@RequestBody ReqPostVerifyEmailDto requestDto) {

        ResponseDto<Void> responseDto = ResponseDto.of(200, "이메일 인증이 요청되었습니다. 메일을 확인해주세요.", null);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/email-verification/confirm")
    public ResponseEntity<ResponseDto<Void>> confirmEmail(@RequestBody ReqPostConfirmEmailDto requestDto) {

        ResponseDto<Void> responseDto = ResponseDto.of(200, "이메일 인증이 처리되었습니다.", null);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<ResPostLoginDto>> login(@RequestBody ReqPostLoginDto requestDto) {

        ResPostLoginDto resPostLoginDto = ResPostLoginDto.builder()
                .accessToken("exampleAccessToken")
                .refreshToken("exampleRefreshToken")
                .build();

        ResponseDto<ResPostLoginDto> responseDto = ResponseDto.of(200, "로그인 성공", resPostLoginDto);

        return ResponseEntity.ok(responseDto);
    }
}
