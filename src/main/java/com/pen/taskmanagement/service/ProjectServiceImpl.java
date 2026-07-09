package com.pen.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;
import com.pen.taskmanagement.exceptions.ForbiddenException;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.ProjectMapper;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.ProjectRepository;
import com.pen.taskmanagement.repository.UserRepository;
import com.pen.taskmanagement.utilities.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {

        List<User> userIds = (projectRequest.userIds() != null) ? (userRepository.findAllById(projectRequest.userIds()))
                : (List.of());
        Project project = projectMapper.toEntity(projectRequest);
        project.setUsers(userIds);
        project.setStartTime(LocalDateTime.now());
        project.setCreatedBy(securityUtil.extractUsername());
        return projectMapper.toResponse(projectRepository.save(project));

    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> readProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse readProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return projectMapper.toResponse(project);

    }

    @Override
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (securityUtil.extractUsername().equals(project.getCreatedBy()) || securityUtil.isAdmin()) {
            projectRepository.deleteById(id);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

    }

    @Override
    public ProjectResponse updateProject(ProjectRequest projectRequest, Long id) {

        List<User> userIds;
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        if (projectRequest.userIds() != null) {
            userIds = userRepository.findAllById(projectRequest.userIds());
        } else {
            userIds = project.getUsers();
        }

        if (securityUtil.extractUsername().equals(project.getCreatedBy()) || securityUtil.isAdmin()) {
            project.setName(projectRequest.name());
            project.setDescription(projectRequest.description());
            project.setEndTime(projectRequest.endDateTime());
            project.setUsers(userIds);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

        return projectMapper.toResponse(projectRepository.save(project));
    }
}
