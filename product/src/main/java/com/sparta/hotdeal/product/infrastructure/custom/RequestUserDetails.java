package com.sparta.hotdeal.product.infrastructure.custom;

import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class RequestUserDetails implements UserDetails {
    @Getter
    private final UUID userId;

    @Getter
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public RequestUserDetails(String userId, String email, Collection<? extends GrantedAuthority> authorities) {
        this.userId = UUID.fromString(userId);
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public String getRole() {
        return authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
    }
}