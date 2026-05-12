package com.pen.taskmanagement.mapper;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.model.User;

public class UserMapper {

    public User toEntity(UserRequest userRequest) {
        User user = new User();

        user.setEmail(userRequest.email());
        user.setName(userRequest.name());
        user.setSurname(userRequest.surname());
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());

        return user;

    }

    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse(user.getName(),user.getSurname(),user.getEmail());

        return userResponse;

        
    }
}
