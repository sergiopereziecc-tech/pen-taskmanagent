package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;

public record ApiError(
    String message,
    String path,
    LocalDateTime timestamp
) {
    
}
