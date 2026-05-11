package com.pen.taskmanagement.service;

import java.util.List;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;
import com.pen.taskmanagement.model.Project;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest projectRequest);

    List<ProjectResponse> readProjects();
    
    ProjectResponse readProject(Long id);

    void deleteProject(Long id);

    ProjectResponse updateProject(ProjectRequest projectRequest,Long id);
}
