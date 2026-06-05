package com.pen.taskmanagement.utilities;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public String extractUsername(){

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
