package com.pen.taskmanagement.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pen.taskmanagement.dtos.ProjectRequest;
import com.pen.taskmanagement.dtos.ProjectResponse;
import com.pen.taskmanagement.model.Project;
import com.pen.taskmanagement.model.ProjectStatus;
import com.pen.taskmanagement.model.User;

@Component
public class ProjectMapper {
    
    public Project toEntity(ProjectRequest projectRequest){
        
        Project project = new Project();

        project.setName(projectRequest.name());
        project.setDescription(projectRequest.description());
        project.setStartTime(projectRequest.starDateTime());
        project.setEndTime(projectRequest.endDateTime());
        project.setStatus(ProjectStatus.PENDING);


        return project;
    }

    public ProjectResponse toResponse(Project project){
        List<String> usernames = project.getUsers().stream().map(User::getUsername)
        .toList();

        ProjectResponse projectResponse = new ProjectResponse(
            project.getName(),project.getDescription(),project.getStartTime(),
            project.getEndTime(), project.getStatus(),usernames
        );
        return projectResponse;
    }

}
