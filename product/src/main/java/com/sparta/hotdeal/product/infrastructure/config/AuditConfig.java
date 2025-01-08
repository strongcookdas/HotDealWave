package com.sparta.hotdeal.product.infrastructure.config;

import com.sparta.hotdeal.product.infrastructure.custom.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {
    @Bean
    public SecurityAuditorAware securityAuditorAware() {
        return new SecurityAuditorAware();
    }
}