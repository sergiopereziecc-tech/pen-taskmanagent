package com.pen.taskmanagement.utilities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public String extractUsername(){

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isAdmin(){

        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
        .stream().anyMatch(authorities -> authorities.getAuthority().equals("ROLE_ADMIN"));
    }
}
