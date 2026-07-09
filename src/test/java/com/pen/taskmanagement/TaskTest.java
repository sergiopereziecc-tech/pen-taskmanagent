package com.pen.taskmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;
import com.pen.taskmanagement.exceptions.ForbiddenException;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.TaskMapper;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.RoleEnum;
import com.pen.taskmanagement.model.Task;
import com.pen.taskmanagement.model.TaskStatus;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.ProjectRepository;
import com.pen.taskmanagement.repository.TaskRepository;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.service.TaskServiceImpl;

import com.pen.taskmanagement.utilities.SecurityUtil;



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

    @Mock
    SecurityUtil securityUtil;
    @InjectMocks
    TaskServiceImpl taskServiceImpl;

    @Test
    void shouldCreate() {

        TaskRequest request = new TaskRequest("Task Name", "Description here", LocalDateTime.now(), 1L, 1L);
        User user = new User();
        Project project = new Project();
        project.setId(1L);
        project.setName("Main");
        project.setCreatedBy("tornado");
        Task task = new Task();
        task.setId(1L);
        task.setName("Task");
        TaskResponse taskResponse = new TaskResponse(null,
                null, null, null, null, null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtil.extractUsername()).thenReturn("tornado");
        when(taskMapper.toEntity(request, user, project)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse taskResponse2 = taskServiceImpl.createTask(request);

        assertEquals(taskResponse, taskResponse2);
        verify(taskRepository, times(1)).save(task);

    }

    @Test
    void shouldUpdateSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setName("Sergio");
        user.setUsername("tornado");

        Project project = new Project();
        project.setId(1L);
        project.setName("Main");

        Task task = new Task();

        task.setId(1L);
        task.setName("Handler");
        task.setUser(user);

        TaskRequest request = new TaskRequest(null, null, null, 1L, 1L);

        TaskResponse taskResponse = new TaskResponse(null, null, null, null, null, null, null, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtil.extractUsername()).thenReturn("tornado");
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse taskResponse2 = taskServiceImpl.updateTask(request, task.getId());

        assertEquals(taskResponse, taskResponse2);
    }

    @Test
    void shouldNotUpdate() {

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

        assertThrows(ResourceNotFoundException.class, () -> taskServiceImpl.updateTask(taskRequest, 99L));

    }

    @Test
    void deleteNotSuccessfully() {

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskServiceImpl.deleteTask(99L));
    }

    @Test
    void shouldThrowForbiddenWhenDeletingTaskWithWrongUser() {

        Project project = new Project();
        project.setCreatedBy("1tr");

        User user = new User(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "123",
                "password123",
                RoleEnum.USER,
                List.of(),
                List.of());

        Task task = new Task(
                1L,
                "Implement JWT Authentication",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "Implement login and token generation",
                TaskStatus.PENDING,
                user,
                project);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtil.extractUsername()).thenReturn("tornado");

        assertThrows(ForbiddenException.class, () -> taskServiceImpl.deleteTask(1L));
    }

}
