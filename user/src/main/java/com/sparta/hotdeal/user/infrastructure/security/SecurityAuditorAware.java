package com.sparta.hotdeal.user.infrastructure.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof String) {
            return Optional.of("anonymousUser");
        }

        RequestUserDetails userDetails = (RequestUserDetails) authentication.getPrincipal();

        return Optional.of(userDetails.getEmail());
    }
}
