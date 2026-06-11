package com.pen.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pen.taskmanagement.model.RoleEnum;
import com.pen.taskmanagement.utilities.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/project").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/project/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/project/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/task").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/task/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/task/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/user/{id}").hasRole("ADMIN")
                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
