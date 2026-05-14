package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(
    @NotBlank
    String name,
    @NotBlank
    String description,
    LocalDateTime endDateTime,
    Long userId,
    @NotNull
    Long projectId

) {
    
}
