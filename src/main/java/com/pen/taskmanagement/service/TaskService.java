package com.pen.taskmanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;

public interface TaskService {

    TaskResponse createTask(TaskRequest taskRequest);
    
    Page<TaskResponse> readAllTask(Pageable pageable);

    TaskResponse readTask(Long id);

    void deleteTask(Long id);

    TaskResponse updateTask(TaskRequest taskRequest, Long id);
    

}
