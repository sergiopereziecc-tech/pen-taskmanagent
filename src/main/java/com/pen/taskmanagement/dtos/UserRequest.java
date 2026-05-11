package com.pen.taskmanagement.dtos;

public record UserRequest(
    String name,
    String surname,
    String email,
    String username,
    String password
) {
    
}
