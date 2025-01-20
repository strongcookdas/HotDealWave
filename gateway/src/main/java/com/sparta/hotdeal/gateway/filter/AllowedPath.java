package com.sparta.hotdeal.gateway.filter;

import java.util.EnumSet;

public enum AllowedPath {
    SWAGGER("/swagger-ui/", "*"),
    API_DOCS("/v3/api-docs", "*"),
    AUTH("/api/v1/auth", "*"),
    PAYMENT("/payment/", "*"),
    PRODUCTS("/api/v1/products", "GET"),
    PROMOTIONS("/api/v1/promotions", "GET"),
    REVIEWS("/api/v1/reviews", "GET");

    private final String path;
    private final String method;

    AllowedPath(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static boolean isAllowed(String requestPath, String requestMethod) {
        return EnumSet.allOf(AllowedPath.class).stream()
                .anyMatch(allowedPath -> allowedPath.matches(requestPath, requestMethod));
    }

    private boolean matches(String requestPath, String requestMethod) {
        return requestPath.startsWith(this.path) &&
                ("*".equals(this.method) || this.method.equalsIgnoreCase(requestMethod));
    }
}
