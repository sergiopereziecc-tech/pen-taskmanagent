package com.pen.taskmanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pen.taskmanagement.dtos.AuthResponse;
import com.pen.taskmanagement.dtos.LogInRequest;
import com.pen.taskmanagement.dtos.LogInResponse;
import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.service.UserSecurityService;
import com.pen.taskmanagement.service.UserService;
import com.pen.taskmanagement.utilities.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserSecurityService userSecurityService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRequest request) {

        userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("ACCOUNT CREATED"));

    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> login(@RequestBody  LogInRequest request) {
        
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()));
        
        UserDetails user =  userSecurityService.loadUserByUsername(request.username());

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.status(HttpStatus.OK).body(new LogInResponse(request.username(), token));

        
        
    }

}
