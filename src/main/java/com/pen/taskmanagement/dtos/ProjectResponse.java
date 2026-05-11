package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.pen.taskmanagement.model.ProjectStatus;

public record ProjectResponse(
    String name,
    String description,
    LocalDateTime starDateTime,
    LocalDateTime endDateTime,
    ProjectStatus status,
    List<String> userNames
) {
    
}
