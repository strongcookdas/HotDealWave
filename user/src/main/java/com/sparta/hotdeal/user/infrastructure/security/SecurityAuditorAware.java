package com.sparta.hotdeal.user.infrastructure.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
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
