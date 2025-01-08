package com.sparta.hotdeal.product.infrastructure.custom;

import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class RequestUserDetails implements UserDetails {
    @Getter
    private final UUID userId;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public RequestUserDetails(String userId, String username, Collection<? extends GrantedAuthority> authorities) {
        this.userId = UUID.fromString(userId);
        this.username = username;
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
        return username;
    }

    public String getRole() {
        return authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
    }
}