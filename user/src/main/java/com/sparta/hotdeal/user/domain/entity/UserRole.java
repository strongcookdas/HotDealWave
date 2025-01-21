package com.sparta.hotdeal.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    CUSTOMER(Authority.CUSTOMER),  // 구매자 권한
    SELLER(Authority.SELLER),  // 판매자 권한
    MANAGER(Authority.MANAGER), // 매니저 권한
    MASTER(Authority.MASTER); // 마스터 권한

    private final String authority;

    public static class Authority {
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String SELLER = "ROLE_SELLER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MASTER = "ROLE_MASTER";
    }

    @JsonCreator
    public static UserRole fromString(String role) {
        return UserRole.valueOf(role.toUpperCase()); // 대소문자 구분을 하지 않음
    }

    @JsonValue
    public String toValue() {
        return this.name(); // name()은 enum의 이름을 반환 (CUSTOMER, SELLER 등)
    }
}
