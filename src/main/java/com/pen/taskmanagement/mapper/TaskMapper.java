package com.pen.taskmanagement.mapper;

import org.springframework.stereotype.Component;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.Task;
import com.pen.taskmanagement.model.TaskStatus;
import com.pen.taskmanagement.model.User;

@Component
public class TaskMapper {
    
    public Task toEntity(TaskRequest taskRequest,User user, Project project){

        Task task = new Task();

        task.setName(taskRequest.name());
        task.setDescription(taskRequest.description());
        task.setStartTime(taskRequest.startDateTime());
        task.setEndTime(taskRequest.endDateTime());
        task.setUser(user);
        task.setProject(project);
        task.setStatus(TaskStatus.PENDING);

        return task;
    }

    public TaskResponse toResponse(Task task){
        TaskResponse taskResponse = new TaskResponse(task.getName(),
        task.getDescription(), task.getStartTime(),task.getEndTime(),
        task.getUser()!= null ? task.getUser().getUsername() : null ,task.getUser()!= null ? task.getUser().getName() : null,
        task.getUser()!= null ? task.getUser().getSurname() : null,
        task.getStatus(), task.getProject().getName()
        );
        return taskResponse;

    }

    
}
