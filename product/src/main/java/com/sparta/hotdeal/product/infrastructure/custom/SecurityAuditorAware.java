package com.sparta.hotdeal.product.infrastructure.custom;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {//username으로 저장 (userId는 초기 생성값이 없기 때문)
        // SecurityContext에서 Authentication 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 인증되지 않은 경우 기본값 반환
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(
                authentication.getPrincipal())) {
            return Optional.of("SYSTEM"); // 스케줄러 또는 비인증 작업의 기본값
        }

        // 인증된 사용자 정보 가져오기
        if (authentication.getPrincipal() instanceof RequestUserDetails) {
            RequestUserDetails userDetails = (RequestUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getEmail());
        }

        return Optional.of("SYSTEM"); // 예상치 못한 경우에도 기본값 반환
    }
}