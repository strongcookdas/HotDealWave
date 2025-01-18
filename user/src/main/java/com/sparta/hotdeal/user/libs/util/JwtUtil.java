package com.sparta.hotdeal.user.libs.util;

import com.sparta.hotdeal.user.application.exception.CustomJwtExcpetion;
import com.sparta.hotdeal.user.application.exception.JwtExceptionMessage;
import com.sparta.hotdeal.user.domain.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "jwt")
@Component
public class JwtUtil {
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    @Value("${service.jwt.access-expiration}")
    private long ACCESS_TOKEN_TIME;

    @Value("${service.jwt.refresh-expiration}")
    private long REFRESH_TOKEN_TIME;

    @Value("${service.jwt.secret-key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(UUID userId, String email, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .claim("userId", userId.toString()) // 사용자 식별자값(userId)
                        .claim("email", email)
                        .claim("role", role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                        .compact();
    }

    public String createRefreshToken(UUID userId) {
        Date date = new Date();

        return Jwts.builder()
                .claim("userId", userId.toString())
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error(JwtExceptionMessage.INVALID_JWT_SIGNATURE.getMessage());
            throw new CustomJwtExcpetion(JwtExceptionMessage.INVALID_JWT_SIGNATURE.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(JwtExceptionMessage.EXPIRED_JWT_TOKEN.getMessage());
            throw new CustomJwtExcpetion(JwtExceptionMessage.EXPIRED_JWT_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(JwtExceptionMessage.UNSUPPORTED_JWT_TOKEN.getMessage());
            throw new CustomJwtExcpetion(JwtExceptionMessage.UNSUPPORTED_JWT_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(JwtExceptionMessage.JWT_CLAIM_IS_EMPTY.getMessage());
            throw new CustomJwtExcpetion(JwtExceptionMessage.JWT_CLAIM_IS_EMPTY.getMessage());
        }
    }
}
