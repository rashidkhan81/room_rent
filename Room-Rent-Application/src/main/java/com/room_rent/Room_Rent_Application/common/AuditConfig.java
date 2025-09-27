package com.room_rent.Room_Rent_Application.common;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//
//import java.util.Optional;
//
//@Configuration
//public class AuditConfig {
//
//    @Bean
//    public AuditorAware<Long> auditorProvider() {
//        return () -> {
//            // TODO: Replace with your actual user fetching logic
//            // For now, returning a dummy user ID
//            return Optional.of(1L);
//        };
//    }
//}


import com.room_rent.Room_Rent_Application.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        // Assuming you are returning UserDetails with a getId() method
        User user = (User) auth.getPrincipal();
        return Optional.of(user.getId());
    }
}


