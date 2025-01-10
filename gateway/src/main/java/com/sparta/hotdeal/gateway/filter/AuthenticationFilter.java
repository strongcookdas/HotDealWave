package com.sparta.hotdeal.gateway.filter;

import com.sparta.hotdeal.gateway.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        if (checkRequestIsNotRequireFilter(path, method)) {
            return chain.filter(exchange);
        }

        String token = jwtUtil.extractToken(exchange);

        if (token == null || !jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 토큰 검증 성공 시 헤더 덮어쓰기
        Claims claims = jwtUtil.parseClaims(token);
        // 새 요청 객체를 만들어서 헤더를 설정하고, 기존 exchange의 요청으로 교체
        exchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-UserId", claims.get("userId", String.class))
                        .header("X-User-Email", claims.get("email", String.class))
                        .header("X-User-Role", claims.get("role", String.class))
                        .build())
                .build();

        return chain.filter(exchange);
    }

    // 추후 리팩토링 진행
    private boolean checkRequestIsNotRequireFilter(String path, String method) {
        //swagger 경로
        if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) {
            return true;
        }

        // auth 경로
        if (path.startsWith("/api/v1/auth")) {
            return true;
        }

        // payment 경로
        if (path.startsWith("/api/v1/payment")) {
            return true;
        }

        // product 경로
        if (path.startsWith("/api/v1/products") && method.equalsIgnoreCase("GET")) {
            return true;
        }

        // promotion 경로
        if (path.startsWith("/api/v1/promotions") && method.equalsIgnoreCase("GET")) {
            return true;
        }

        // review 경로
        if (path.startsWith("/api/v1/reviews") && method.equalsIgnoreCase("GET")) {
            return true;
        }

        return false;
    }
}
