package com.pen.taskmanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pen.taskmanagement.dtos.TaskRequest;
import com.pen.taskmanagement.dtos.TaskResponse;
import com.pen.taskmanagement.service.TaskService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/new")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        
        
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @GetMapping("/all/tasks")
    public ResponseEntity<List<TaskResponse>> getListTasks() {
        return ResponseEntity.ok(taskService.readAllTask());
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.readTask(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(request, id));
    }
    
    
}
