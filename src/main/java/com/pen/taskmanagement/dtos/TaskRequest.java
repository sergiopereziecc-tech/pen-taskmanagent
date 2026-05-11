package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;

public record TaskRequest(
    String name,
    String description,
    LocalDateTime starDateTime,
    LocalDateTime endDateTime,
    Long userId,
    Long projectId

) {
    
}
