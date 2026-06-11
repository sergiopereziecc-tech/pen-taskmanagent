package com.pen.taskmanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;
import com.pen.taskmanagement.model.Project;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest projectRequest);

    Page<ProjectResponse> readProjects(Pageable pageable);
    
    ProjectResponse readProject(Long id);

    void deleteProject(Long id);

    ProjectResponse updateProject(ProjectRequest projectRequest,Long id);
}
