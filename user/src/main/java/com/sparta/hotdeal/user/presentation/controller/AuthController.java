package com.sparta.hotdeal.user.presentation.controller;

import com.sparta.hotdeal.user.application.dtos.ResponseDto;
import com.sparta.hotdeal.user.application.dtos.ResponseMessage;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostCheckEmailDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostConfirmEmailDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostLoginDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostRefreshDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostSignUpDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostVerifyEmailDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostLoginDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostRefreshDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostSignUpDto;
import com.sparta.hotdeal.user.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostSignUpDto> signUp(@RequestBody ReqPostSignUpDto requestDto) {

        ResPostSignUpDto resPostSignUpDto = authService.signup(requestDto);

        return ResponseDto.of(ResponseMessage.SIGNUP_SUCCESS.getMessage(), resPostSignUpDto);
    }

    @PostMapping("/check-email")
    public ResponseDto<Void> checkEmail(@RequestBody ReqPostCheckEmailDto requestDto) {

        authService.checkEmail(requestDto.getEmail());

        return ResponseDto.of(ResponseMessage.USABLE_EMAIL.getMessage(), null);
    }

    @PostMapping("/email-verification")
    public ResponseDto<Void> sendVerifyEmail(@RequestBody ReqPostVerifyEmailDto requestDto) {

        authService.sendVerifyEmail(requestDto);

        return ResponseDto.of(ResponseMessage.EMAIL_SENT.getMessage(), null);
    }

    @PostMapping("/email-verification/confirm")
    public ResponseDto<Void> confirmEmail(@RequestBody ReqPostConfirmEmailDto requestDto) {

        authService.confirmEmail(requestDto);

        return ResponseDto.of(ResponseMessage.EMAIL_CONFIRMED.getMessage(), null);
    }

    @PostMapping("/login")
    public ResponseDto<ResPostLoginDto> login(@RequestBody ReqPostLoginDto requestDto) {

        ResPostLoginDto resPostLoginDto = authService.login(requestDto.getEmail(), requestDto.getPassword());

        return ResponseDto.of(ResponseMessage.LOGIN_SUCCESS.getMessage(), resPostLoginDto);
    }

    @PostMapping("/refresh")
    public ResponseDto<ResPostRefreshDto> refresh(@RequestBody ReqPostRefreshDto requestDto) {

        ResPostRefreshDto resPostRefreshDto = ResPostRefreshDto.builder()
                .accessToken("exampleAccessToken")
                .refreshToken("exampleRefreshToken")
                .build();

        return ResponseDto.of("토큰 재발급 성공", resPostRefreshDto);
    }
}
