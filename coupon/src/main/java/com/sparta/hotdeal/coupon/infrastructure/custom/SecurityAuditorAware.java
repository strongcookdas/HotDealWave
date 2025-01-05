package com.sparta.hotdeal.coupon.infrastructure.custom;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {//username으로 저장 (userId는 초기 생성값이 없기 때문)
        RequestUserDetails userDetails = (RequestUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (userDetails != null) {
            return Optional.of(userDetails.getUsername());
        }

        return Optional.of("anonymousUser");
    }
}
