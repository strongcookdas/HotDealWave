package com.sparta.hotdeal.coupon.infrastructure.filter;

import com.sparta.hotdeal.coupon.infrastructure.custom.RequestUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
            throws ServletException, IOException { //userId 이외에 다른 데이터가 필요한지 논의 필요

        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-User-Name"); // 중복 허용되지 않는 값으로 설정이 필요 (일단 email로 생각하고 구현)
        String role = request.getHeader("X-User-Role"); //ROLE_ 추가되는지 확인 필요

        if (userId == null || role == null) {
            /* 로그인 연동 전까지 mock user 사용
            log.warn("Missing authentication headers: X-User-Id or X-User-Role");
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
             */
            userId = "8fbd655f-dc52-4bf9-ab23-ef89e923db44";
            username = "mock@email.com";
            role = "MASTER";
        }

        // ROLE_ 접두사 추가
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UserDetails userDetails = new RequestUserDetails(userId, username, authorities);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
