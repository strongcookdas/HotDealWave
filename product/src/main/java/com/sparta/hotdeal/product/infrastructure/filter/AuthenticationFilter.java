package com.sparta.hotdeal.product.infrastructure.filter;

import com.sparta.hotdeal.product.infrastructure.custom.RequestUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.security.sasl.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String role = request.getHeader("X-User-Role");

        // 특정 경로(Swagger 등)는 필터링 제외
        String requestURI = request.getRequestURI();
        if ((requestURI.startsWith("/api/v1/products") && "GET".equalsIgnoreCase(request.getMethod())) ||
                (requestURI.startsWith("/api/v1/promotions") && "GET".equalsIgnoreCase(request.getMethod())) ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (userId == null || role == null) {
            /* 로그인 연동 전까지 mock user 사용
            log.warn("Missing authentication headers: X-User-Id or X-User-Role");
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
             */
//            userId = "8fbd655f-dc52-4bf9-ab23-ef89e923db44";
//            email = "mock@email.com";
//            role = "MASTER";
            throw new AuthenticationException("Missing authentication headers");
        }

        // ROLE_ 접두사 추가
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UserDetails userDetails = new RequestUserDetails(userId, email, authorities);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
