package com.room_rent.Room_Rent_Application.jwtTokenUtils;

import com.room_rent.Room_Rent_Application.model.Role;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {


    private final UserRepository userRepository;

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            // auth.getName() usually contains the email/username
            String email = auth.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("User not authenticated");
    }

    public Long getUserIdFromToken() {
        return getLoggedInUser().getId();
    }

    /**
     * Check if the logged-in user has the given role name.
     */
    public boolean hasRole(String roleName) {
        User user = getLoggedInUser();
        return user.getRole().name().equalsIgnoreCase(roleName);
    }

    public boolean hasRole(Role role) {
        User user = getLoggedInUser();
        return user.getRole() == role;
    }


}
