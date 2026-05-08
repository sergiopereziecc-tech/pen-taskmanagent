package com.pen.taskmanagement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;


@Entity

public class Task {
    
    private Long id;
    private String name;
    private LocalDateTime timestamp;

}
