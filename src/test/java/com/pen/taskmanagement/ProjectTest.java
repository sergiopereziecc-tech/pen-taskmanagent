package com.pen.taskmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;
import com.pen.taskmanagement.exceptions.ForbiddenException;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.ProjectMapper;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.repository.ProjectRepository;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.service.ProjectServiceImpl;
import com.pen.taskmanagement.utilities.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class ProjectTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    ProjectMapper projectMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityUtil securityUtil;

    @InjectMocks
    ProjectServiceImpl projectServiceImpl;

    @Test
    void shouldCreate() {

        ProjectRequest request = new ProjectRequest("Main", "Description", null, null);
        Project project = new Project();
        project.setId(1L);
        project.setName("Main");
        ProjectResponse projectResponse = new ProjectResponse(null, null, null, null, null, null);

        when(projectMapper.toEntity(request)).thenReturn(project);
        when(securityUtil.extractUsername()).thenReturn("tornado");
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(projectResponse);

        ProjectResponse result = projectServiceImpl.createProject(request);

        assertEquals(projectResponse, result);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void shouldUpdateSuccessfullyAsCreator() {

        Project project = new Project();
        project.setId(1L);
        project.setCreatedBy("tornado");
        project.setUsers(List.of());

        ProjectRequest request = new ProjectRequest("New name", "New description", null, null);
        ProjectResponse projectResponse = new ProjectResponse(null, null, null, null, null, null);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtil.extractUsername()).thenReturn("tornado");
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(projectResponse);

        ProjectResponse result = projectServiceImpl.updateProject(request, 1L);

        assertEquals(projectResponse, result);
    }

    @Test
    void shouldUpdateSuccessfullyAsAdmin() {

        Project project = new Project();
        project.setId(1L);
        project.setCreatedBy("owner");
        project.setUsers(List.of());

        ProjectRequest request = new ProjectRequest("New name", "New description", null, null);
        ProjectResponse projectResponse = new ProjectResponse(null, null, null, null, null, null);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtil.extractUsername()).thenReturn("admin");
        when(securityUtil.isAdmin()).thenReturn(true);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(projectResponse);

        ProjectResponse result = projectServiceImpl.updateProject(request, 1L);

        assertEquals(projectResponse, result);
    }

    @Test
    void shouldThrowForbiddenWhenUpdatingWithWrongUser() {

        Project project = new Project();
        project.setId(1L);
        project.setCreatedBy("owner");

        ProjectRequest request = new ProjectRequest("New name", "New description", null, null);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtil.extractUsername()).thenReturn("intruder");
        when(securityUtil.isAdmin()).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> projectServiceImpl.updateProject(request, 1L));
    }

    @Test
    void shouldNotUpdateWhenNotFound() {

        ProjectRequest request = new ProjectRequest("New name", "New description", null, null);

        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectServiceImpl.updateProject(request, 99L));
    }

    @Test
    void shouldDeleteSuccessfullyAsCreator() {

        Project project = new Project();
        project.setId(1L);
        project.setCreatedBy("tornado");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtil.extractUsername()).thenReturn("tornado");

        projectServiceImpl.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowForbiddenWhenDeletingWithWrongUser() {

        Project project = new Project();
        project.setId(1L);
        project.setCreatedBy("owner");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(securityUtil.extractUsername()).thenReturn("intruder");
        when(securityUtil.isAdmin()).thenReturn(false);

        assertThrows(ForbiddenException.class, () -> projectServiceImpl.deleteProject(1L));
    }

    @Test
    void shouldNotDeleteWhenNotFound() {

        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectServiceImpl.deleteProject(99L));
    }
}
