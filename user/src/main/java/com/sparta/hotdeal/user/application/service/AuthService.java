package com.sparta.hotdeal.user.application.service;

import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostConfirmEmailDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostSignUpDto;
import com.sparta.hotdeal.user.application.dtos.auth.request.ReqPostVerifyEmailDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostLoginDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostRefreshDto;
import com.sparta.hotdeal.user.application.dtos.auth.response.ResPostSignUpDto;
import com.sparta.hotdeal.user.application.exception.CustomJwtExcpetion;
import com.sparta.hotdeal.user.application.exception.ErrorMessage;
import com.sparta.hotdeal.user.application.util.JwtUtil;
import com.sparta.hotdeal.user.application.util.RedisUtil;
import com.sparta.hotdeal.user.domain.entity.Email;
import com.sparta.hotdeal.user.domain.entity.User;
import com.sparta.hotdeal.user.domain.entity.UserRole;
import com.sparta.hotdeal.user.domain.repository.EmailRepository;
import com.sparta.hotdeal.user.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final static String MAIL_TITLE = "핫딜 이커머스 서비스 인증번호";

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Value("${service.jwt.refresh-expiration}")
    private long refreshTokenExpirationMillis;

    @Transactional
    public ResPostSignUpDto signup(ReqPostSignUpDto requestDto) {
        Email email = emailRepository.findById(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.EMAIL_NOT_VERIFIED.getMessage()));

        checkEmailIsVerified(email);
        checkNickname(requestDto.getNickname());
        checkRole(requestDto.getRole());

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = requestDto.toEntity(encodedPassword);
        userRepository.save(user);
        user.updateCreatedByAndUpdateBy(user.getEmail());

        return ResPostSignUpDto.builder()
                .userId(user.getUserId())
                .build();
    }

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_EMAIL.getMessage());
        }
    }

    @Transactional
    public void sendVerifyEmail(ReqPostVerifyEmailDto requestDto) {
        checkEmail(requestDto.getEmail());

        if (!emailRepository.existsById(requestDto.getEmail())) {
            Email email = requestDto.toEntity();
            emailRepository.save(email);
        }

        String code = createCode();
        mailService.sendEmail(requestDto.getEmail(), MAIL_TITLE, code);
        redisUtil.setValues(requestDto.getEmail(), code, authCodeExpirationMillis);
    }

    @Transactional
    public void confirmEmail(ReqPostConfirmEmailDto requestDto) {
        String code = redisUtil.getValues(requestDto.getEmail());

        Email email = emailRepository.findById(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.EMAIL_NOT_VERIFIED.getMessage()));

        if (!code.equals(requestDto.getVerificationToken())) {
            throw new IllegalArgumentException(ErrorMessage.WRONG_VERIFICATION_CODE.getMessage());
        }

        email.updateVerified();
        redisUtil.deleteValues(requestDto.getEmail());
    }

    public ResPostLoginDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessage.WRONG_EMAIL_OR_PASSWORD.getMessage()));

        checkDeletedUser(user);

        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        redisUtil.setValues(user.getUserId().toString(), refreshToken, refreshTokenExpirationMillis);

        return ResPostLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ResPostRefreshDto refresh(
            String requestRefreshToken
    ) {
        jwtUtil.validateToken(requestRefreshToken);
        Claims token = jwtUtil.parseClaims(requestRefreshToken);

        UUID requestUserId = UUID.fromString(token.get("userId", String.class));

        String currentRefreshToken = redisUtil.getValues(requestUserId.toString());
        checkExpiredRefreshToken(currentRefreshToken, requestRefreshToken);

        User user = userRepository.findById(requestUserId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        checkDeletedUser(user);

        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        redisUtil.setValues(requestUserId.toString(), refreshToken, refreshTokenExpirationMillis);

        return ResPostRefreshDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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

    private void checkEmailIsVerified(Email email) {
        if (!email.isVerified()) {
            throw new IllegalArgumentException(ErrorMessage.EMAIL_NOT_VERIFIED.getMessage());
        }
    }

    private void checkDeletedUser(User user) {
        if (user.getDeletedAt() != null) {
            throw new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage());
        }
    }

    private String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private void checkExpiredRefreshToken(String currentRefreshToken, String requestRefreshToken) {
        if (currentRefreshToken == null || !currentRefreshToken.equals(requestRefreshToken)) {
            throw new IllegalArgumentException(ErrorMessage.EXPIRED_REFRESH_TOKEN.getMessage());
        }
    }

    private void checkJwtError(String refreshToken) {

    }
}
