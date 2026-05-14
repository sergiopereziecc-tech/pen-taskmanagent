package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.pen.taskmanagement.model.ProjectStatus;
import com.pen.taskmanagement.model.User;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
    @NotBlank
    String name,
    @NotBlank
    String description,
    LocalDateTime endDateTime,
    List<Long> userIds
    
) {
    
}
