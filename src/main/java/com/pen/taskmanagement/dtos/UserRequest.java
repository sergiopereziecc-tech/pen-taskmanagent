package com.pen.taskmanagement.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
    @NotBlank
    String name,
    @NotBlank
    String surname,
    @NotBlank
    @Email
    String email,
    @NotBlank
    String username,
    @NotBlank
    String password 
) {
    
}
