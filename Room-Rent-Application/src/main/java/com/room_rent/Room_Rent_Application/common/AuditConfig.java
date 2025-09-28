package com.room_rent.Room_Rent_Application.common;


import com.room_rent.Room_Rent_Application.service.ForSecurityCreatedByAndAll.CustomUserDetails;
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

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails customUser) {
            return Optional.of(customUser.getId()); // ✅ now createdBy/modifiedBy will work
        }

        return Optional.empty();
    }


}