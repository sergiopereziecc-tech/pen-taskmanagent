package com.pen.taskmanagement.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.pen.taskmanagement.model.ProjectStatus;
import com.pen.taskmanagement.model.User;

public record ProjectRequest(
    String name,
    String description,
    LocalDateTime starDateTime,
    LocalDateTime endDateTime,
    List<Long> userIds
    
) {
    
}
