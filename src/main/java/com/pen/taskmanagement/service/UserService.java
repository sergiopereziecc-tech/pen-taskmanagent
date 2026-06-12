package com.pen.taskmanagement.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;


public interface UserService {
    

    UserResponse createUser(UserRequest userRequest);

    Page<UserResponse> readAllUsers(Pageable pageable);

    UserResponse readUser(Long id);

    void deleteUser(Long id);

    UserResponse updateUser(UserRequest userRequest,Long id);
}
