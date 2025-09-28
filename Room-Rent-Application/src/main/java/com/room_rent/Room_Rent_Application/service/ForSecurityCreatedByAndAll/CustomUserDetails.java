package com.room_rent.Room_Rent_Application.service.ForSecurityCreatedByAndAll;


import com.room_rent.Room_Rent_Application.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


// newly created to store the abstract all the datas
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Get role from user and convert to GrantedAuthority
        return List.of(user.getRole().name()).stream()
                .map(r -> (GrantedAuthority) () -> r)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getEmail(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return user.isEnabled(); }

    public Long getId() { return user.getId(); } // <-- for AuditorAware
}

