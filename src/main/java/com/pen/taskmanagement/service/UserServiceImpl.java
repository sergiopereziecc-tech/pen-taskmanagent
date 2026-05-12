package com.pen.taskmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pen.taskmanagement.dtos.UserRequest;
import com.pen.taskmanagement.dtos.UserResponse;
import com.pen.taskmanagement.exceptions.ResourceNotFoundException;
import com.pen.taskmanagement.mapper.UserMapper;
import com.pen.taskmanagement.model.User;
import com.pen.taskmanagement.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        
        return userMapper.toResponse(userRepository.save(user));
        
    }

    @Override
    public List<UserResponse> readAllUsers() {
        List<UserResponse> users = userRepository.findAll()
            .stream().map(userMapper::toResponse).toList();
        
        return users;
    }

    @Override
    public UserResponse readUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User not found");

        userRepository.deleteById(id);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));


        user.setName(userRequest.name());
        user.setSurname(userRequest.surname());
        user.setEmail(userRequest.email());
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());

        return userMapper.toResponse(userRepository.save(user));

    }

}
