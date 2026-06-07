package com.pen.taskmanagement.mapper;

import org.springframework.stereotype.Component;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.model.RoleEnum;
import com.pen.taskmanagement.model.User;

@Component
public class UserMapper {

    public User toEntity(UserRequest userRequest) {
        User user = new User();

        user.setEmail(userRequest.email());
        user.setName(userRequest.name());
        user.setSurname(userRequest.surname());
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());
        user.setRole(RoleEnum.USER);

        return user;

    }

    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse(user.getName(),user.getSurname(),user.getEmail());

        return userResponse;

        
    }
}
