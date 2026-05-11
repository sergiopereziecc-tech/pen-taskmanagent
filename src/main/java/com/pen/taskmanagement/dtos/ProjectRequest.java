package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;

import com.pen.taskmanagement.model.ProjectStatus;

public record ProjectRequest(
    String name,
    String description,
    LocalDateTime starDateTime,
    LocalDateTime endDateTime
    
) {
    
}
