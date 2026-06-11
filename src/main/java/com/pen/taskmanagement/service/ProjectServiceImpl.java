package com.pen.taskmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;
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
    public Page<ProjectResponse> readProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toResponse);

    }

    @Override
    public ProjectResponse readProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return projectMapper.toResponse(project);

    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id))
            throw new ResourceNotFoundException("Project not found");

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (securityUtil.extractUsername().equals(project.getCreatedBy())) {
            projectRepository.deleteById(id);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

    }

    @Override
    public ProjectResponse updateProject(ProjectRequest projectRequest, Long id) {
        List<User> userIds = userRepository.findAllById(projectRequest.userIds());
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (securityUtil.extractUsername().equals(project.getCreatedBy())) {
            project.setName(projectRequest.name());
            project.setDescription(projectRequest.description());
            // project.setStartTime(projectRequest.starDateTime());
            project.setEndTime(projectRequest.endDateTime());
            project.setUsers(userIds);
        } else {
            throw new ForbiddenException("Permissions not found");
        }

        return projectMapper.toResponse(projectRepository.save(project));
    }
}
