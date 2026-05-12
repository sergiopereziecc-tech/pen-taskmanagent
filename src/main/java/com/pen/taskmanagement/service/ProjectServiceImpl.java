package com.pen.taskmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.ProjectMapper;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.ProjectRepository;
import com.pen.taskmanagement.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        List<User> userIds = userRepository.findAllById(projectRequest.userIds());
        Project project = projectMapper.toEntity(projectRequest);
        project.setUsers(userIds);
        return projectMapper.toResponse(projectRepository.save(project));

    }

    @Override
    public List<ProjectResponse> readProjects() {
        List<ProjectResponse> projects = projectRepository.findAll()
                .stream().map(projectMapper::toResponse)
                .toList();
        return projects;
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

        projectRepository.deleteById(id);
    }

    @Override
    public ProjectResponse updateProject(ProjectRequest projectRequest, Long id) {
        List<User> userIds = userRepository.findAllById(projectRequest.userIds());
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        project.setName(projectRequest.name());
        project.setDescription(projectRequest.description());
        project.setStartTime(projectRequest.starDateTime());
        project.setEndTime(projectRequest.endDateTime());
        project.setUsers(userIds);

        return projectMapper.toResponse(projectRepository.save(project));
    }
}
