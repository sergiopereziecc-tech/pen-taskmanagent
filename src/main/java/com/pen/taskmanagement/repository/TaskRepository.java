package com.pen.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pen.taskmanagement.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
    
}
