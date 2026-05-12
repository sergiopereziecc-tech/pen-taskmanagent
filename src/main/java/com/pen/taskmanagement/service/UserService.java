package com.pen.taskmanagement.service;

import java.util.List;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.model.User;

public interface UserService {
    

    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> readAllUsers();

    UserResponse readUser(Long id);

    void deleteUser(Long id);

    UserResponse updateUser(UserRequest userRequest,Long id);
}
