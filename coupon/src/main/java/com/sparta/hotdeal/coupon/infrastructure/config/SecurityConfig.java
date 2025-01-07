package com.sparta.hotdeal.coupon.infrastructure.config;

import com.sparta.hotdeal.coupon.infrastructure.exception.handler.CustomAccessDeniedHandler;
import com.sparta.hotdeal.coupon.infrastructure.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**",       // Swagger UI 관련 정적 리소스
                                "/v3/api-docs/**",      // OpenAPI 문서
                                "/swagger-ui.html",     // Swagger HTML 경로
                                "/swagger-resources/**", // Swagger 설정 리소스
                                "/webjars/**"           // Swagger 정적 리소스
                        ).permitAll()
                        .anyRequest().authenticated() // 모든 요청 인증 필요 헤더가 없으면 403 리턴하기 위해
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한 부족(403) 시 핸들러
                )
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 커스텀 필터 추가
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }
}