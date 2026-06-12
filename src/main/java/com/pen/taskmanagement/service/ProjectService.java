package com.pen.taskmanagement.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;


public interface ProjectService {

    ProjectResponse createProject(ProjectRequest projectRequest);

    Page<ProjectResponse> readProjects(Pageable pageable);
    
    ProjectResponse readProject(Long id);

    void deleteProject(Long id);

    ProjectResponse updateProject(ProjectRequest projectRequest,Long id);
}
