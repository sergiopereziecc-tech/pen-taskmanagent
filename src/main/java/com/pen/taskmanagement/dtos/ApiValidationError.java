package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiValidationError(
    String message,
    String path,
    LocalDateTime timestamp,
    Map<String, String> fieldErrors
) {
    
}
