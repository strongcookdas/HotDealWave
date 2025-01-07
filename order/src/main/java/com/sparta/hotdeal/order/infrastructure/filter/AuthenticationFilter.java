package com.sparta.hotdeal.order.infrastructure.filter;

import com.sparta.hotdeal.order.infrastructure.custom.RequestUserDetails;
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
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        log.info("userId : {}", userId);
        String email = request.getHeader("X-User-Email");
        log.info("email : {}", email);
        String role = request.getHeader("X-User-Role");
        log.info("role : {}", role);

        if (userId == null || email == null || role == null) {
            log.warn("Missing authentication headers");
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
//            userId = "8fbd655f-dc52-4bf9-ab23-ef89e923db44";
//            email = "mock@email.com";
//            role = "MASTER";
        }

//        if (!role.startsWith("ROLE_")) {
//            role = "ROLE_" + role;
//        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UserDetails userDetails = new RequestUserDetails(userId, email, authorities);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

