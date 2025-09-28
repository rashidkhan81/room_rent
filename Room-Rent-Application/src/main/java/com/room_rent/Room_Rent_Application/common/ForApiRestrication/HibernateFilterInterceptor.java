package com.room_rent.Room_Rent_Application.common.ForApiRestrication;

import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HibernateFilterInterceptor extends OncePerRequestFilter {

    private final EntityManager entityManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Get current user id
        Long currentUserId = SecurityUtils.getCurrentUserId();

        if (currentUserId != null) {
            Session session = entityManager.unwrap(Session.class);
            org.hibernate.Filter filter = session.enableFilter("createdByFilter");
            filter.setParameter("userId", currentUserId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clean up after request
            entityManager.unwrap(Session.class).disableFilter("createdByFilter");
        }
    }

}
