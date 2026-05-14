package com.pen.taskmanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;


    @PostMapping("/new")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.readUser(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getUsersList() {
        return ResponseEntity.ok(userService.readAllUsers());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request){
        return ResponseEntity.ok(userService.updateUser(request, id));
    }
    


}
