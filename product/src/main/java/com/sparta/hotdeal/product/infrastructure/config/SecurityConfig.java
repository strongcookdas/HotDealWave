package com.sparta.hotdeal.product.infrastructure.config;

import com.sparta.hotdeal.product.infrastructure.exception.handler.CustomAccessDeniedHandler;
import com.sparta.hotdeal.product.infrastructure.exception.handler.CustomAuthenticationHandler;
import com.sparta.hotdeal.product.infrastructure.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //spring security 설정 활성화
@EnableMethodSecurity(securedEnabled = true) //Secured 사용을 위해
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationHandler())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한 부족(403) 시 핸들러
                )
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 커스텀 필터 추가

        return http.build();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }
}