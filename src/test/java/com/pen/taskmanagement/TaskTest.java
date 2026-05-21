package com.pen.taskmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.TaskMapper;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.Task;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.ProjectRepository;
import com.pen.taskmanagement.repository.TaskRepository;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.service.TaskServiceImpl;
import com.pen.taskmanagement.service.UserService;

import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
public class TaskTest {

    @Mock
    TaskMapper taskMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    TaskServiceImpl taskServiceImpl;

    @Test
    void shouldCreate() {

        TaskRequest request = new TaskRequest("Task Name", "Description here", LocalDateTime.now(), 1L, 1L);
        User user = new User();
        Project project = new Project();
        project.setId(1L);
        project.setName("Main");
        Task task = new Task();
        task.setId(1L);
        task.setName("Task");
        TaskResponse taskResponse = new TaskResponse(null,
                null, null, null, null, null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskMapper.toEntity(request, user, project)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse taskResponse2 = taskServiceImpl.createTask(request);

        assertEquals(taskResponse, taskResponse2);
        verify(taskRepository, times(1)).save(task);

    }

    @Test
    void shouldUpdateSuccessfully(){

        User user = new User();
        user.setId(1L);
        user.setName("Sergio");

        Project project = new Project();
        project.setId(1L);
        project.setName("Main");

        Task task = new Task();

        task.setId(1L);
        task.setName("Handler");

        TaskRequest request = new TaskRequest(null, null, null, 1L  , 1L);

        TaskResponse taskResponse = new TaskResponse(null, null, null, null, null, null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse taskResponse2 = taskServiceImpl.updateTask(request, task.getId());

        assertEquals(taskResponse, taskResponse2);
    }

    @Test
    void shouldNotUpdate(){

        TaskRequest taskRequest = new TaskRequest(null, null, null, 1L, 1L);
        User user = new User();
        user.setId(1L);
        user.setName("Sergio");
        Project project = new Project();
        project.setId(1L);
        project.setName("Main");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> taskServiceImpl.updateTask(taskRequest, 99L));

    }

    @Test
    void deleteNotSuccessfully(){
        

        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskServiceImpl.deleteTask(99L));
    }

    

}
