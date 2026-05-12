package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;

public record TaskRequest(
    String name,
    String description,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    Long userId,
    Long projectId

) {
    
}
