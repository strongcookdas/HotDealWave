package com.sparta.hotdeal.payment.infrastructure.custom;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        RequestUserDetails userDetails = (RequestUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (userDetails != null) {
            return Optional.of(userDetails.getEmail());
        }

        return Optional.of("anonymousUser");
    }
}
