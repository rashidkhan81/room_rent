package com.room_rent.Room_Rent_Application.config.constant;

public class SecurityConstant {

    public static final String[] PUBLIC_MATCHERS = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/verify-otp",
            "/api/auth/resend-otp",
            "/forgot-password",
            "/reset-password",
            "/logout",
            "/api/public/**"
    };

    public static final String[] SWAGGER_MATCHERS = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/configuration/ui",
            "/configuration/security",
            "/oauth2/swagger_login"
    };
}

