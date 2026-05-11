package com.pen.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pen.taskmanagement.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>{
    
}
