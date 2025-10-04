package com.room_rent.Room_Rent_Application.controller.currentLoggedUser;

import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.security.JwtUtil;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import com.room_rent.Room_Rent_Application.service.room.RoomServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/current-logged")
@RequiredArgsConstructor
public class CurrentLoggedUser {

    private final JwtUtil jwtUtil;
    private final RoomServiceImpl roomService;

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


//    @GetMapping("/top-rated")
//    public ResponseEntity<?> getTopRatedRooms() {
//        List<RoomResponseProjection> topRooms = roomService.recommendTopRatedRooms();
//        return ResponseEntity.ok(Map.of(
//                "status", true,
//                "message", "Top Rated Rooms Fetched Successfully",
//                "data", topRooms
//        ));
//    }
}
