package com.pen.taskmanagement.service;

import java.util.List;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;

public interface TaskService {

    TaskResponse createTask(TaskRequest taskRequest);
    
    List<TaskResponse> readAllTask();

    TaskResponse readTask(Long id);

    void deleteTask(Long id);

    TaskResponse updateTask(TaskRequest taskRequest, Long id);
    

}
