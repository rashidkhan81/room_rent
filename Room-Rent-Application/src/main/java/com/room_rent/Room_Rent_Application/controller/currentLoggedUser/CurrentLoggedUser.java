package com.room_rent.Room_Rent_Application.controller.currentLoggedUser;

import com.room_rent.Room_Rent_Application.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/current-logged")
@RequiredArgsConstructor
public class CurrentLoggedUser {

    private final JwtUtil jwtUtil;

    @GetMapping("/user")
    public Map<String, Object> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validate(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Claims claims = jwtUtil.validateAndExtractClaims(token);

        Map<String, Object> response = new HashMap<>();
        response.put("email", claims.getSubject());
        response.put("userId", claims.get("userId", Long.class));
        response.put("name", claims.get("name", String.class));
        response.put("role", claims.get("role", String.class));
        response.put("expiresAt", claims.getExpiration());

        return response;
    }
}
