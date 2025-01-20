package com.sparta.hotdeal.coupon.infrastructure.filter;

import com.sparta.hotdeal.coupon.infrastructure.custom.RequestUserDetails;
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

        String userId = request.getHeader("X-User-UserId");
        String email = request.getHeader("X-User-Email");
        String role = request.getHeader("X-User-Role");
        // 특정 경로(Swagger 등)는 필터링 제외
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/webjars") ||
                requestURI.startsWith("/actuator/prometheus")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (userId == null || email == null || role == null) {
            log.warn("Missing authentication headers");
            // 다음 필터로 넘어가서 security 에서 401 자동 리턴
            //spring security는 context가 비어있을 때 401 리턴
//            SecurityContextHolder.clearContext();
//            filterChain.doFilter(request, response);
//            return;

            //mock user
//            userId = "8fbd655f-dc52-4bf9-ab23-ef89e923db44";
//            email = "mock@email.com";
//            role = "MASTER";

            //401 핸들러에서 처리
            throw new AuthenticationException("Missing authentication headers");
        }

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