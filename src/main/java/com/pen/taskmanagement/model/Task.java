package com.pen.taskmanagement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    
    private Long id;
    private String name;
    private LocalDateTime starTime;
    private LocalDateTime endTime;


    

}
