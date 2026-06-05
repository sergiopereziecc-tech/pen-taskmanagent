package com.pen.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;
import com.pen.taskmanagement.exceptions.ForbiddenException;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.TaskMapper;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.Task;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.ProjectRepository;
import com.pen.taskmanagement.repository.TaskRepository;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.utilities.SecurityUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final SecurityUtil securityUtil;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        User user = (taskRequest.userId() != null)
                ? (userRepository.findById(taskRequest.userId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")))
                : (null);

        Project project = projectRepository.findById(taskRequest.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = taskMapper.toEntity(taskRequest, user, project);
        task.setStartTime(LocalDateTime.now());
        TaskResponse taskResponse = taskMapper.toResponse(taskRepository.save(task));

        return taskResponse;
    }

    @Override
    public List<TaskResponse> readAllTask() {
        List<TaskResponse> tasks = taskRepository.findAll()
                .stream().map(taskMapper::toResponse).toList();
        return tasks;
    }

    @Override
    public TaskResponse readTask(Long id) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        TaskResponse taskResponse = taskMapper.toResponse(task);

        return taskResponse;
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id))
            throw new ResourceNotFoundException("Task not found");

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        String username = securityUtil.extractUsername();

        if (username.equals(task.getUser().getUsername())) {
            taskRepository.deleteById(id);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

    }

    @Override
    public TaskResponse updateTask(TaskRequest taskRequest, Long id) {
        User user = userRepository.findById(taskRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = projectRepository.findById(taskRequest.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        String username = securityUtil.extractUsername();

        if (username.equals(task.getUser().getUsername())) {
            task.setName(taskRequest.name());
            task.setDescription(taskRequest.description());
            // task.setStartTime(taskRequest.startDateTime());
            task.setEndTime(taskRequest.endDateTime());
            task.setProject(project);
            task.setUser(user);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

}
