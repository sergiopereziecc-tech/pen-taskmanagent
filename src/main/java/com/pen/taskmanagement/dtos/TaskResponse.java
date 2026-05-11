package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;

import com.pen.taskmanagement.model.TaskStatus;

public record TaskResponse(
    String name,
    String description,
    LocalDateTime starDateTime,
    LocalDateTime endDateTime,
    String username,
    String userFirstname,
    String userSurname,
    TaskStatus status,
    String projectName
) {
    
}
